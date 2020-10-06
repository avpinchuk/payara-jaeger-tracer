# Jaeger's Tracing Instrumentation Library for Payara Platform

 * Intended to be used with [Jaeger](https://github.com/jaegertracing/jaeger) backend.
 * Implements [Opentracing Java API 0.31.0](https://github.com/opentracing/opentracing-java/tree/release-0.31.0).
 * Supports Java 1.8 and above.
 * Supports Payara Platform 5.194 and above.
 * Supports configuration with [Microprofile Config 1.3](https://github.com/eclipse/microprofile-config/tree/1.3).
 * Exposes Jaeger internal metrics with [Microprofile Metrics 2.2](https://github.com/eclipse/microprofile-metrics/tree/2.2).
 
### Building

```sh
mvn clean package
```
To save a bit of time, you can skip tests by appending the skipTests flag to the package command like so:

```sh
mvn clean package -DskipTests
```

Artifacts resides under `jaeger-tracer`/`target`.
   
### Configuration

Configuration performed via Microprofile Config.

When obtaining a tracer instance, values specified via system properties will
override values specified via environment variables, and values specified via environment variables will
override values specified via `microprofile-config.properties` config file. Overriding performed according to
config sources priorities.

The following property names are available:
 
Microprofile Config Property | Default Value | Description
--- | --- | ---
mp.opentracing.jaeger.service-name | application name | The service name
mp.opentracing.jaeger.agent-host | localhost | The hostname for communicating with agent via UDP
mp.opentracing.jaeger.agent-port | 6831 | The port for communicating with agent via UDP
mp.opentracing.jaeger.endpoint | none | The traces endpoint, in case the client should connect directly to the Collector, like http://jaeger-collector:14268/api/traces
mp.opentracing.jaeger.auth-token | none | Authentication Token to send as "Bearer" to the endpoint
mp.opentracing.jaeger.user | none | Username to send as part of "Basic" authentication to the endpoint
mp.opentracing.jaeger.password | none | Password to send as part of "Basic" authentication to the endpoint
mp.opentracing.jaeger.propagation | jaeger | Comma separated list of formats to use for propagation the trace context. Defaults to the standard Jaeger format. Valid values are **jaeger**, **b3** and **w3c**
mp.opentracing.jaeger.reporter-log-spans | false | Whether the reporter should also log the spans
mp.opentracing.jaeger.reporter-max-queue-size | 100 | The reporter's maximum queue size
mp.opentracing.jaeger.reporter-flush-interval | 1000 | The reporter's flush interval (ms)
mp.opentracing.jaeger.sampler-type | remote | The sampler type
mp.opentracing.jaeger.sampler-param | none | The sampler parameter (number)
mp.opentracing.jaeger.sampler-manager-host-port | localhost:5778 | The hostname and port when using the remote controlled sampler
mp.opentracing.jaeger.tags | none | A comma separated list of `name = value` tracer level tags, which get added to all reported spans. The value can also refer to an environment variable using the format `${envVarName:default}`, where the `:default` is optional, and identifies a value to be used if the environment variable cannot be found
mp.opentracing.jaeger.traceid-128bit | false | Use 128 bit Trace ID
mp.opentracing.jaeger.sender-factory | none | When there are multiple service providers for the `SenderFactory` available, this property is used to select a `SenderFactory` by matching it with one returned by `SenderFactory.getType()`

None properties required.
 
Setting `jaeger-agent-host`/`jaeger-agent-port` will make the client send traces to the agent via
`UdpSender`. If `jaeger-endpoint` is also set, the traces are sent to the endpoint, effectively making
the `jaeger-agent-*` ineffective.
 
When `JAEGER_ENDPOINT` is set, the `HttpSender` is used when submitting traces to a remote endpoint,
usually served by a Jaeger Collector. If the endpoint is secured, a HTTP Basic Authentication can be
performed by setting the related properties. Similarly, if the endpoint expects an Authentication Token,
like a JWT, set the `jaeger-auth-token` property. If the Basic Authentication properties *and* Authentication
Token property are set, Basic Authentication is used.

##### Sampling Configuration

The following samplers is available:

Sampler Type | Description
--- | ---
const | Constant sampler always makes the same decision for all traces. It either samples all traces (`sampler-param=1`) or none of them (`sampler-param=0`)
probabilistic | Probabilistic sampler makes a random sampling decision with the probability of sampling equal to the value of `sampler-param` property. For example, with `sampler-param=0.1` approximately 1 in 10 traces will be sampled
ratelimiting | Rate Limiting sampler uses a leaky bucket rate limiter to ensure that traces are sampled with a certain constant rate. For example, when `sampler-param=2.0` it will sample requests with the rate of 2 traces per second
remote | Remote sampler consults Jaeger agent for the appropriate sampling strategy to use in the current service. This allows controlling the sampling strategies in the services from a [central configuration](https://www.jaegertracing.io/docs/1.19/sampling/#collector-sampling-configuration) in Jaeger backend, or even dynamically (see [Adaptive Sampling](https://www.jaegertracing.io/docs/1.19/sampling/#adaptive-sampler))

### Microprofile Metrics Integration

Metrics are exposed under application scope.

The following metrics is available:

Metric | Metric Type | Description
--- | --- | ---
jaeger.tracer.traces;sampled=y;state=started | counter | Number of traces started by this tracer as sampled
jaeger.tracer.traces;sampled=n;state=started | counter | Number of traces started by this tracer as not sampled
jaeger.tracer.traces;sampled=y;state=joined | counter | Number of externally started sampled traces this tracer joined
jaeger.tracer.traces;sampled=n;state=joined | counter | Number of externally started unsampled traces this tracer joined
jaeger.tracer.started.spans;sampled=y | counter | Number of sampled spans started by this tracer
jaeger.tracer.started.spans;sampled=n | counter | Number of unsampled spans started by this tracer
jaeger.tracer.finished.spans | counter | Number of spans finished by this tracer
jaeger.tracer.span.context.decoding.errors | counter | Number of errors decoding tracing context
jaeger.tracer.reporter.spans;result=ok | counter | Number of successfully reported spans
jaeger.tracer.reporter.spans;result=err | counter | Number of spans not reported due to a `Sender` failure
jaeger.tracer.reporter.spans;result=dropped | counter | Number of spans dropped due to internal queue overflow
jaeger.tracer.reporter.queue.length | gauge | Current number of spans in the reporter queue
jaeger.tracer.sampler.queries;result=ok | counter | Number of times the `Sampler` succeeded to retrieve sampling strategy
jaeger.tracer.sampler.queries;result=err | counter | Number of times the `Sampler` failed to retrieve sampling strategy
jaeger.tracer.sampler.updates;result=ok | counter | Number of times the `Sampler` succeeded to retrieve and update sampling strategy
jaeger.tracer.sampler.updates;result=err | counter | Number of times the `Sampler` failed to update sampling strategy
jaeger.tracer.baggage.updates;result=ok | counter | Number of times baggage was successfully written or updated on spans
jaeger.tracer.baggage.updates;result=err | counter | Number of times baggage failed to write or update on spans
jaeger.tracer.baggage.truncations | counter | Number of times baggage was truncated as per baggage restrictions
jaeger.tracer.baggage.restrictions.updates;result=ok | counter | Number of times baggage restrictions were successfully updated
jaeger.tracer.baggage.restrictions.updates;result=err | counter | Number of times baggage restrictions failed to update

### Using with Payara Platform

##### Install library

This must be added as a library to the server itself, not included with a deployed application.

*Add it as a library with the asadmin command*

```sh
asadmin add-library jaeger-tracer-all.jar
```

*Add it to a new Payara Micro Instance*

```sh
java -jar payare-micro.jar --addLibs jaeger-tracer-all.jar
```

##### Enable request tracing feature

*With the asadmin command*

```sh
asadmin set-requesttracing-configuration --enabled=true --dynamic==true

asadmin bootstrap-requesttracing
```

*Using postboot script*

Create postboot script file, e.g. `postboot.asadmin with` following content:

```sh
set-requesttracing-configuration --enabled=true --dynamic=true
bootstrap-requesttracing
```

Run Payara Micro:

```sh
java -jar payara-micro.jar --postbootcommandfile postboot.asadmin
```

*Programmatically*

Create a singleton EJB bean in your application:

```java
@javax.ejb.Singleton
@javax.ejb.Startup
public class RequestTracingBean {

    @javax.annotation.PostConstruct
    public void init() {
        PayaraMicroRuntime runtime = PayaraMicro.getInstance().getRuntime();
        runtime.run("set-requesttracing-configuration", "--enabled=true", "--dynamic=true");
        runtime.run("bootstrap-requesttracing");
    }
    
}
```

### Implementation Notes

During building of the project generated two artifacts: shaded and unshaded versions of library.
In shaded version of library all externals packages which not belong to the project are relocated to internal
namespace, thus avoiding conflicts with packages included in other modules and applications.

### License

[Apache 2.0 License](./LICENSE). 