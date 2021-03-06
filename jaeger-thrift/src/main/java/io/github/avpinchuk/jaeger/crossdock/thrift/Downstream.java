/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.github.avpinchuk.jaeger.crossdock.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class Downstream implements org.apache.thrift.TBase<Downstream, Downstream._Fields>, java.io.Serializable, Cloneable, Comparable<Downstream> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Downstream");

  private static final org.apache.thrift.protocol.TField SERVICE_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("serviceName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField SERVER_ROLE_FIELD_DESC = new org.apache.thrift.protocol.TField("serverRole", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField HOST_FIELD_DESC = new org.apache.thrift.protocol.TField("host", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("port", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField TRANSPORT_FIELD_DESC = new org.apache.thrift.protocol.TField("transport", org.apache.thrift.protocol.TType.I32, (short)5);
  private static final org.apache.thrift.protocol.TField DOWNSTREAM_FIELD_DESC = new org.apache.thrift.protocol.TField("downstream", org.apache.thrift.protocol.TType.STRUCT, (short)6);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DownstreamStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DownstreamTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String serviceName; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String serverRole; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String host; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String port; // required
  /**
   * 
   * @see Transport
   */
  public @org.apache.thrift.annotation.Nullable Transport transport; // required
  public @org.apache.thrift.annotation.Nullable Downstream downstream; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SERVICE_NAME((short)1, "serviceName"),
    SERVER_ROLE((short)2, "serverRole"),
    HOST((short)3, "host"),
    PORT((short)4, "port"),
    /**
     * 
     * @see Transport
     */
    TRANSPORT((short)5, "transport"),
    DOWNSTREAM((short)6, "downstream");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SERVICE_NAME
          return SERVICE_NAME;
        case 2: // SERVER_ROLE
          return SERVER_ROLE;
        case 3: // HOST
          return HOST;
        case 4: // PORT
          return PORT;
        case 5: // TRANSPORT
          return TRANSPORT;
        case 6: // DOWNSTREAM
          return DOWNSTREAM;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final _Fields optionals[] = {_Fields.DOWNSTREAM};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SERVICE_NAME, new org.apache.thrift.meta_data.FieldMetaData("serviceName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SERVER_ROLE, new org.apache.thrift.meta_data.FieldMetaData("serverRole", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.HOST, new org.apache.thrift.meta_data.FieldMetaData("host", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PORT, new org.apache.thrift.meta_data.FieldMetaData("port", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TRANSPORT, new org.apache.thrift.meta_data.FieldMetaData("transport", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, Transport.class)));
    tmpMap.put(_Fields.DOWNSTREAM, new org.apache.thrift.meta_data.FieldMetaData("downstream", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT        , "Downstream")));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Downstream.class, metaDataMap);
  }

  public Downstream() {
  }

  public Downstream(
    java.lang.String serviceName,
    java.lang.String serverRole,
    java.lang.String host,
    java.lang.String port,
    Transport transport)
  {
    this();
    this.serviceName = serviceName;
    this.serverRole = serverRole;
    this.host = host;
    this.port = port;
    this.transport = transport;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Downstream(Downstream other) {
    if (other.isSetServiceName()) {
      this.serviceName = other.serviceName;
    }
    if (other.isSetServerRole()) {
      this.serverRole = other.serverRole;
    }
    if (other.isSetHost()) {
      this.host = other.host;
    }
    if (other.isSetPort()) {
      this.port = other.port;
    }
    if (other.isSetTransport()) {
      this.transport = other.transport;
    }
    if (other.isSetDownstream()) {
      this.downstream = new Downstream(other.downstream);
    }
  }

  public Downstream deepCopy() {
    return new Downstream(this);
  }

  @Override
  public void clear() {
    this.serviceName = null;
    this.serverRole = null;
    this.host = null;
    this.port = null;
    this.transport = null;
    this.downstream = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getServiceName() {
    return this.serviceName;
  }

  public Downstream setServiceName(@org.apache.thrift.annotation.Nullable java.lang.String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  public void unsetServiceName() {
    this.serviceName = null;
  }

  /** Returns true if field serviceName is set (has been assigned a value) and false otherwise */
  public boolean isSetServiceName() {
    return this.serviceName != null;
  }

  public void setServiceNameIsSet(boolean value) {
    if (!value) {
      this.serviceName = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getServerRole() {
    return this.serverRole;
  }

  public Downstream setServerRole(@org.apache.thrift.annotation.Nullable java.lang.String serverRole) {
    this.serverRole = serverRole;
    return this;
  }

  public void unsetServerRole() {
    this.serverRole = null;
  }

  /** Returns true if field serverRole is set (has been assigned a value) and false otherwise */
  public boolean isSetServerRole() {
    return this.serverRole != null;
  }

  public void setServerRoleIsSet(boolean value) {
    if (!value) {
      this.serverRole = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getHost() {
    return this.host;
  }

  public Downstream setHost(@org.apache.thrift.annotation.Nullable java.lang.String host) {
    this.host = host;
    return this;
  }

  public void unsetHost() {
    this.host = null;
  }

  /** Returns true if field host is set (has been assigned a value) and false otherwise */
  public boolean isSetHost() {
    return this.host != null;
  }

  public void setHostIsSet(boolean value) {
    if (!value) {
      this.host = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getPort() {
    return this.port;
  }

  public Downstream setPort(@org.apache.thrift.annotation.Nullable java.lang.String port) {
    this.port = port;
    return this;
  }

  public void unsetPort() {
    this.port = null;
  }

  /** Returns true if field port is set (has been assigned a value) and false otherwise */
  public boolean isSetPort() {
    return this.port != null;
  }

  public void setPortIsSet(boolean value) {
    if (!value) {
      this.port = null;
    }
  }

  /**
   * 
   * @see Transport
   */
  @org.apache.thrift.annotation.Nullable
  public Transport getTransport() {
    return this.transport;
  }

  /**
   * 
   * @see Transport
   */
  public Downstream setTransport(@org.apache.thrift.annotation.Nullable Transport transport) {
    this.transport = transport;
    return this;
  }

  public void unsetTransport() {
    this.transport = null;
  }

  /** Returns true if field transport is set (has been assigned a value) and false otherwise */
  public boolean isSetTransport() {
    return this.transport != null;
  }

  public void setTransportIsSet(boolean value) {
    if (!value) {
      this.transport = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public Downstream getDownstream() {
    return this.downstream;
  }

  public Downstream setDownstream(@org.apache.thrift.annotation.Nullable Downstream downstream) {
    this.downstream = downstream;
    return this;
  }

  public void unsetDownstream() {
    this.downstream = null;
  }

  /** Returns true if field downstream is set (has been assigned a value) and false otherwise */
  public boolean isSetDownstream() {
    return this.downstream != null;
  }

  public void setDownstreamIsSet(boolean value) {
    if (!value) {
      this.downstream = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case SERVICE_NAME:
      if (value == null) {
        unsetServiceName();
      } else {
        setServiceName((java.lang.String)value);
      }
      break;

    case SERVER_ROLE:
      if (value == null) {
        unsetServerRole();
      } else {
        setServerRole((java.lang.String)value);
      }
      break;

    case HOST:
      if (value == null) {
        unsetHost();
      } else {
        setHost((java.lang.String)value);
      }
      break;

    case PORT:
      if (value == null) {
        unsetPort();
      } else {
        setPort((java.lang.String)value);
      }
      break;

    case TRANSPORT:
      if (value == null) {
        unsetTransport();
      } else {
        setTransport((Transport)value);
      }
      break;

    case DOWNSTREAM:
      if (value == null) {
        unsetDownstream();
      } else {
        setDownstream((Downstream)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case SERVICE_NAME:
      return getServiceName();

    case SERVER_ROLE:
      return getServerRole();

    case HOST:
      return getHost();

    case PORT:
      return getPort();

    case TRANSPORT:
      return getTransport();

    case DOWNSTREAM:
      return getDownstream();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case SERVICE_NAME:
      return isSetServiceName();
    case SERVER_ROLE:
      return isSetServerRole();
    case HOST:
      return isSetHost();
    case PORT:
      return isSetPort();
    case TRANSPORT:
      return isSetTransport();
    case DOWNSTREAM:
      return isSetDownstream();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Downstream)
      return this.equals((Downstream)that);
    return false;
  }

  public boolean equals(Downstream that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_serviceName = true && this.isSetServiceName();
    boolean that_present_serviceName = true && that.isSetServiceName();
    if (this_present_serviceName || that_present_serviceName) {
      if (!(this_present_serviceName && that_present_serviceName))
        return false;
      if (!this.serviceName.equals(that.serviceName))
        return false;
    }

    boolean this_present_serverRole = true && this.isSetServerRole();
    boolean that_present_serverRole = true && that.isSetServerRole();
    if (this_present_serverRole || that_present_serverRole) {
      if (!(this_present_serverRole && that_present_serverRole))
        return false;
      if (!this.serverRole.equals(that.serverRole))
        return false;
    }

    boolean this_present_host = true && this.isSetHost();
    boolean that_present_host = true && that.isSetHost();
    if (this_present_host || that_present_host) {
      if (!(this_present_host && that_present_host))
        return false;
      if (!this.host.equals(that.host))
        return false;
    }

    boolean this_present_port = true && this.isSetPort();
    boolean that_present_port = true && that.isSetPort();
    if (this_present_port || that_present_port) {
      if (!(this_present_port && that_present_port))
        return false;
      if (!this.port.equals(that.port))
        return false;
    }

    boolean this_present_transport = true && this.isSetTransport();
    boolean that_present_transport = true && that.isSetTransport();
    if (this_present_transport || that_present_transport) {
      if (!(this_present_transport && that_present_transport))
        return false;
      if (!this.transport.equals(that.transport))
        return false;
    }

    boolean this_present_downstream = true && this.isSetDownstream();
    boolean that_present_downstream = true && that.isSetDownstream();
    if (this_present_downstream || that_present_downstream) {
      if (!(this_present_downstream && that_present_downstream))
        return false;
      if (!this.downstream.equals(that.downstream))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetServiceName()) ? 131071 : 524287);
    if (isSetServiceName())
      hashCode = hashCode * 8191 + serviceName.hashCode();

    hashCode = hashCode * 8191 + ((isSetServerRole()) ? 131071 : 524287);
    if (isSetServerRole())
      hashCode = hashCode * 8191 + serverRole.hashCode();

    hashCode = hashCode * 8191 + ((isSetHost()) ? 131071 : 524287);
    if (isSetHost())
      hashCode = hashCode * 8191 + host.hashCode();

    hashCode = hashCode * 8191 + ((isSetPort()) ? 131071 : 524287);
    if (isSetPort())
      hashCode = hashCode * 8191 + port.hashCode();

    hashCode = hashCode * 8191 + ((isSetTransport()) ? 131071 : 524287);
    if (isSetTransport())
      hashCode = hashCode * 8191 + transport.getValue();

    hashCode = hashCode * 8191 + ((isSetDownstream()) ? 131071 : 524287);
    if (isSetDownstream())
      hashCode = hashCode * 8191 + downstream.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Downstream other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetServiceName()).compareTo(other.isSetServiceName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetServiceName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serviceName, other.serviceName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetServerRole()).compareTo(other.isSetServerRole());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetServerRole()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serverRole, other.serverRole);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetHost()).compareTo(other.isSetHost());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHost()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.host, other.host);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPort()).compareTo(other.isSetPort());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPort()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.port, other.port);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetTransport()).compareTo(other.isSetTransport());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTransport()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.transport, other.transport);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetDownstream()).compareTo(other.isSetDownstream());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDownstream()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.downstream, other.downstream);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Downstream(");
    boolean first = true;

    sb.append("serviceName:");
    if (this.serviceName == null) {
      sb.append("null");
    } else {
      sb.append(this.serviceName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("serverRole:");
    if (this.serverRole == null) {
      sb.append("null");
    } else {
      sb.append(this.serverRole);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("host:");
    if (this.host == null) {
      sb.append("null");
    } else {
      sb.append(this.host);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("port:");
    if (this.port == null) {
      sb.append("null");
    } else {
      sb.append(this.port);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("transport:");
    if (this.transport == null) {
      sb.append("null");
    } else {
      sb.append(this.transport);
    }
    first = false;
    if (isSetDownstream()) {
      if (!first) sb.append(", ");
      sb.append("downstream:");
      if (this.downstream == null) {
        sb.append("null");
      } else {
        sb.append(this.downstream);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (serviceName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'serviceName' was not present! Struct: " + toString());
    }
    if (serverRole == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'serverRole' was not present! Struct: " + toString());
    }
    if (host == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'host' was not present! Struct: " + toString());
    }
    if (port == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'port' was not present! Struct: " + toString());
    }
    if (transport == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'transport' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DownstreamStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DownstreamStandardScheme getScheme() {
      return new DownstreamStandardScheme();
    }
  }

  private static class DownstreamStandardScheme extends org.apache.thrift.scheme.StandardScheme<Downstream> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Downstream struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SERVICE_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.serviceName = iprot.readString();
              struct.setServiceNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SERVER_ROLE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.serverRole = iprot.readString();
              struct.setServerRoleIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // HOST
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.host = iprot.readString();
              struct.setHostIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.port = iprot.readString();
              struct.setPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // TRANSPORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.transport = Transport.findByValue(iprot.readI32());
              struct.setTransportIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // DOWNSTREAM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.downstream = new Downstream();
              struct.downstream.read(iprot);
              struct.setDownstreamIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Downstream struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.serviceName != null) {
        oprot.writeFieldBegin(SERVICE_NAME_FIELD_DESC);
        oprot.writeString(struct.serviceName);
        oprot.writeFieldEnd();
      }
      if (struct.serverRole != null) {
        oprot.writeFieldBegin(SERVER_ROLE_FIELD_DESC);
        oprot.writeString(struct.serverRole);
        oprot.writeFieldEnd();
      }
      if (struct.host != null) {
        oprot.writeFieldBegin(HOST_FIELD_DESC);
        oprot.writeString(struct.host);
        oprot.writeFieldEnd();
      }
      if (struct.port != null) {
        oprot.writeFieldBegin(PORT_FIELD_DESC);
        oprot.writeString(struct.port);
        oprot.writeFieldEnd();
      }
      if (struct.transport != null) {
        oprot.writeFieldBegin(TRANSPORT_FIELD_DESC);
        oprot.writeI32(struct.transport.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.downstream != null) {
        if (struct.isSetDownstream()) {
          oprot.writeFieldBegin(DOWNSTREAM_FIELD_DESC);
          struct.downstream.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DownstreamTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DownstreamTupleScheme getScheme() {
      return new DownstreamTupleScheme();
    }
  }

  private static class DownstreamTupleScheme extends org.apache.thrift.scheme.TupleScheme<Downstream> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Downstream struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.serviceName);
      oprot.writeString(struct.serverRole);
      oprot.writeString(struct.host);
      oprot.writeString(struct.port);
      oprot.writeI32(struct.transport.getValue());
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetDownstream()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetDownstream()) {
        struct.downstream.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Downstream struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.serviceName = iprot.readString();
      struct.setServiceNameIsSet(true);
      struct.serverRole = iprot.readString();
      struct.setServerRoleIsSet(true);
      struct.host = iprot.readString();
      struct.setHostIsSet(true);
      struct.port = iprot.readString();
      struct.setPortIsSet(true);
      struct.transport = Transport.findByValue(iprot.readI32());
      struct.setTransportIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.downstream = new Downstream();
        struct.downstream.read(iprot);
        struct.setDownstreamIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

