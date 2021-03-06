/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.github.avpinchuk.jaeger.thrift.sampling_manager;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class SamplingStrategyResponse implements org.apache.thrift.TBase<SamplingStrategyResponse, SamplingStrategyResponse._Fields>, java.io.Serializable, Cloneable, Comparable<SamplingStrategyResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SamplingStrategyResponse");

  private static final org.apache.thrift.protocol.TField STRATEGY_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("strategyType", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField PROBABILISTIC_SAMPLING_FIELD_DESC = new org.apache.thrift.protocol.TField("probabilisticSampling", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField RATE_LIMITING_SAMPLING_FIELD_DESC = new org.apache.thrift.protocol.TField("rateLimitingSampling", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField OPERATION_SAMPLING_FIELD_DESC = new org.apache.thrift.protocol.TField("operationSampling", org.apache.thrift.protocol.TType.STRUCT, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SamplingStrategyResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SamplingStrategyResponseTupleSchemeFactory();

  /**
   * 
   * @see SamplingStrategyType
   */
  public @org.apache.thrift.annotation.Nullable SamplingStrategyType strategyType; // required
  public @org.apache.thrift.annotation.Nullable ProbabilisticSamplingStrategy probabilisticSampling; // optional
  public @org.apache.thrift.annotation.Nullable RateLimitingSamplingStrategy rateLimitingSampling; // optional
  public @org.apache.thrift.annotation.Nullable PerOperationSamplingStrategies operationSampling; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see SamplingStrategyType
     */
    STRATEGY_TYPE((short)1, "strategyType"),
    PROBABILISTIC_SAMPLING((short)2, "probabilisticSampling"),
    RATE_LIMITING_SAMPLING((short)3, "rateLimitingSampling"),
    OPERATION_SAMPLING((short)4, "operationSampling");

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
        case 1: // STRATEGY_TYPE
          return STRATEGY_TYPE;
        case 2: // PROBABILISTIC_SAMPLING
          return PROBABILISTIC_SAMPLING;
        case 3: // RATE_LIMITING_SAMPLING
          return RATE_LIMITING_SAMPLING;
        case 4: // OPERATION_SAMPLING
          return OPERATION_SAMPLING;
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
  private static final _Fields optionals[] = {_Fields.PROBABILISTIC_SAMPLING,_Fields.RATE_LIMITING_SAMPLING,_Fields.OPERATION_SAMPLING};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STRATEGY_TYPE, new org.apache.thrift.meta_data.FieldMetaData("strategyType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, SamplingStrategyType.class)));
    tmpMap.put(_Fields.PROBABILISTIC_SAMPLING, new org.apache.thrift.meta_data.FieldMetaData("probabilisticSampling", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ProbabilisticSamplingStrategy.class)));
    tmpMap.put(_Fields.RATE_LIMITING_SAMPLING, new org.apache.thrift.meta_data.FieldMetaData("rateLimitingSampling", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, RateLimitingSamplingStrategy.class)));
    tmpMap.put(_Fields.OPERATION_SAMPLING, new org.apache.thrift.meta_data.FieldMetaData("operationSampling", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PerOperationSamplingStrategies.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SamplingStrategyResponse.class, metaDataMap);
  }

  public SamplingStrategyResponse() {
  }

  public SamplingStrategyResponse(
    SamplingStrategyType strategyType)
  {
    this();
    this.strategyType = strategyType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SamplingStrategyResponse(SamplingStrategyResponse other) {
    if (other.isSetStrategyType()) {
      this.strategyType = other.strategyType;
    }
    if (other.isSetProbabilisticSampling()) {
      this.probabilisticSampling = new ProbabilisticSamplingStrategy(other.probabilisticSampling);
    }
    if (other.isSetRateLimitingSampling()) {
      this.rateLimitingSampling = new RateLimitingSamplingStrategy(other.rateLimitingSampling);
    }
    if (other.isSetOperationSampling()) {
      this.operationSampling = new PerOperationSamplingStrategies(other.operationSampling);
    }
  }

  public SamplingStrategyResponse deepCopy() {
    return new SamplingStrategyResponse(this);
  }

  @Override
  public void clear() {
    this.strategyType = null;
    this.probabilisticSampling = null;
    this.rateLimitingSampling = null;
    this.operationSampling = null;
  }

  /**
   * 
   * @see SamplingStrategyType
   */
  @org.apache.thrift.annotation.Nullable
  public SamplingStrategyType getStrategyType() {
    return this.strategyType;
  }

  /**
   * 
   * @see SamplingStrategyType
   */
  public SamplingStrategyResponse setStrategyType(@org.apache.thrift.annotation.Nullable SamplingStrategyType strategyType) {
    this.strategyType = strategyType;
    return this;
  }

  public void unsetStrategyType() {
    this.strategyType = null;
  }

  /** Returns true if field strategyType is set (has been assigned a value) and false otherwise */
  public boolean isSetStrategyType() {
    return this.strategyType != null;
  }

  public void setStrategyTypeIsSet(boolean value) {
    if (!value) {
      this.strategyType = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public ProbabilisticSamplingStrategy getProbabilisticSampling() {
    return this.probabilisticSampling;
  }

  public SamplingStrategyResponse setProbabilisticSampling(@org.apache.thrift.annotation.Nullable ProbabilisticSamplingStrategy probabilisticSampling) {
    this.probabilisticSampling = probabilisticSampling;
    return this;
  }

  public void unsetProbabilisticSampling() {
    this.probabilisticSampling = null;
  }

  /** Returns true if field probabilisticSampling is set (has been assigned a value) and false otherwise */
  public boolean isSetProbabilisticSampling() {
    return this.probabilisticSampling != null;
  }

  public void setProbabilisticSamplingIsSet(boolean value) {
    if (!value) {
      this.probabilisticSampling = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public RateLimitingSamplingStrategy getRateLimitingSampling() {
    return this.rateLimitingSampling;
  }

  public SamplingStrategyResponse setRateLimitingSampling(@org.apache.thrift.annotation.Nullable RateLimitingSamplingStrategy rateLimitingSampling) {
    this.rateLimitingSampling = rateLimitingSampling;
    return this;
  }

  public void unsetRateLimitingSampling() {
    this.rateLimitingSampling = null;
  }

  /** Returns true if field rateLimitingSampling is set (has been assigned a value) and false otherwise */
  public boolean isSetRateLimitingSampling() {
    return this.rateLimitingSampling != null;
  }

  public void setRateLimitingSamplingIsSet(boolean value) {
    if (!value) {
      this.rateLimitingSampling = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public PerOperationSamplingStrategies getOperationSampling() {
    return this.operationSampling;
  }

  public SamplingStrategyResponse setOperationSampling(@org.apache.thrift.annotation.Nullable PerOperationSamplingStrategies operationSampling) {
    this.operationSampling = operationSampling;
    return this;
  }

  public void unsetOperationSampling() {
    this.operationSampling = null;
  }

  /** Returns true if field operationSampling is set (has been assigned a value) and false otherwise */
  public boolean isSetOperationSampling() {
    return this.operationSampling != null;
  }

  public void setOperationSamplingIsSet(boolean value) {
    if (!value) {
      this.operationSampling = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case STRATEGY_TYPE:
      if (value == null) {
        unsetStrategyType();
      } else {
        setStrategyType((SamplingStrategyType)value);
      }
      break;

    case PROBABILISTIC_SAMPLING:
      if (value == null) {
        unsetProbabilisticSampling();
      } else {
        setProbabilisticSampling((ProbabilisticSamplingStrategy)value);
      }
      break;

    case RATE_LIMITING_SAMPLING:
      if (value == null) {
        unsetRateLimitingSampling();
      } else {
        setRateLimitingSampling((RateLimitingSamplingStrategy)value);
      }
      break;

    case OPERATION_SAMPLING:
      if (value == null) {
        unsetOperationSampling();
      } else {
        setOperationSampling((PerOperationSamplingStrategies)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STRATEGY_TYPE:
      return getStrategyType();

    case PROBABILISTIC_SAMPLING:
      return getProbabilisticSampling();

    case RATE_LIMITING_SAMPLING:
      return getRateLimitingSampling();

    case OPERATION_SAMPLING:
      return getOperationSampling();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case STRATEGY_TYPE:
      return isSetStrategyType();
    case PROBABILISTIC_SAMPLING:
      return isSetProbabilisticSampling();
    case RATE_LIMITING_SAMPLING:
      return isSetRateLimitingSampling();
    case OPERATION_SAMPLING:
      return isSetOperationSampling();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SamplingStrategyResponse)
      return this.equals((SamplingStrategyResponse)that);
    return false;
  }

  public boolean equals(SamplingStrategyResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_strategyType = true && this.isSetStrategyType();
    boolean that_present_strategyType = true && that.isSetStrategyType();
    if (this_present_strategyType || that_present_strategyType) {
      if (!(this_present_strategyType && that_present_strategyType))
        return false;
      if (!this.strategyType.equals(that.strategyType))
        return false;
    }

    boolean this_present_probabilisticSampling = true && this.isSetProbabilisticSampling();
    boolean that_present_probabilisticSampling = true && that.isSetProbabilisticSampling();
    if (this_present_probabilisticSampling || that_present_probabilisticSampling) {
      if (!(this_present_probabilisticSampling && that_present_probabilisticSampling))
        return false;
      if (!this.probabilisticSampling.equals(that.probabilisticSampling))
        return false;
    }

    boolean this_present_rateLimitingSampling = true && this.isSetRateLimitingSampling();
    boolean that_present_rateLimitingSampling = true && that.isSetRateLimitingSampling();
    if (this_present_rateLimitingSampling || that_present_rateLimitingSampling) {
      if (!(this_present_rateLimitingSampling && that_present_rateLimitingSampling))
        return false;
      if (!this.rateLimitingSampling.equals(that.rateLimitingSampling))
        return false;
    }

    boolean this_present_operationSampling = true && this.isSetOperationSampling();
    boolean that_present_operationSampling = true && that.isSetOperationSampling();
    if (this_present_operationSampling || that_present_operationSampling) {
      if (!(this_present_operationSampling && that_present_operationSampling))
        return false;
      if (!this.operationSampling.equals(that.operationSampling))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStrategyType()) ? 131071 : 524287);
    if (isSetStrategyType())
      hashCode = hashCode * 8191 + strategyType.getValue();

    hashCode = hashCode * 8191 + ((isSetProbabilisticSampling()) ? 131071 : 524287);
    if (isSetProbabilisticSampling())
      hashCode = hashCode * 8191 + probabilisticSampling.hashCode();

    hashCode = hashCode * 8191 + ((isSetRateLimitingSampling()) ? 131071 : 524287);
    if (isSetRateLimitingSampling())
      hashCode = hashCode * 8191 + rateLimitingSampling.hashCode();

    hashCode = hashCode * 8191 + ((isSetOperationSampling()) ? 131071 : 524287);
    if (isSetOperationSampling())
      hashCode = hashCode * 8191 + operationSampling.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(SamplingStrategyResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStrategyType()).compareTo(other.isSetStrategyType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStrategyType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.strategyType, other.strategyType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetProbabilisticSampling()).compareTo(other.isSetProbabilisticSampling());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProbabilisticSampling()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.probabilisticSampling, other.probabilisticSampling);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetRateLimitingSampling()).compareTo(other.isSetRateLimitingSampling());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRateLimitingSampling()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.rateLimitingSampling, other.rateLimitingSampling);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetOperationSampling()).compareTo(other.isSetOperationSampling());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOperationSampling()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.operationSampling, other.operationSampling);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SamplingStrategyResponse(");
    boolean first = true;

    sb.append("strategyType:");
    if (this.strategyType == null) {
      sb.append("null");
    } else {
      sb.append(this.strategyType);
    }
    first = false;
    if (isSetProbabilisticSampling()) {
      if (!first) sb.append(", ");
      sb.append("probabilisticSampling:");
      if (this.probabilisticSampling == null) {
        sb.append("null");
      } else {
        sb.append(this.probabilisticSampling);
      }
      first = false;
    }
    if (isSetRateLimitingSampling()) {
      if (!first) sb.append(", ");
      sb.append("rateLimitingSampling:");
      if (this.rateLimitingSampling == null) {
        sb.append("null");
      } else {
        sb.append(this.rateLimitingSampling);
      }
      first = false;
    }
    if (isSetOperationSampling()) {
      if (!first) sb.append(", ");
      sb.append("operationSampling:");
      if (this.operationSampling == null) {
        sb.append("null");
      } else {
        sb.append(this.operationSampling);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (strategyType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'strategyType' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (probabilisticSampling != null) {
      probabilisticSampling.validate();
    }
    if (rateLimitingSampling != null) {
      rateLimitingSampling.validate();
    }
    if (operationSampling != null) {
      operationSampling.validate();
    }
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

  private static class SamplingStrategyResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SamplingStrategyResponseStandardScheme getScheme() {
      return new SamplingStrategyResponseStandardScheme();
    }
  }

  private static class SamplingStrategyResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<SamplingStrategyResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SamplingStrategyResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STRATEGY_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.strategyType = SamplingStrategyType.findByValue(iprot.readI32());
              struct.setStrategyTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PROBABILISTIC_SAMPLING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.probabilisticSampling = new ProbabilisticSamplingStrategy();
              struct.probabilisticSampling.read(iprot);
              struct.setProbabilisticSamplingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // RATE_LIMITING_SAMPLING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.rateLimitingSampling = new RateLimitingSamplingStrategy();
              struct.rateLimitingSampling.read(iprot);
              struct.setRateLimitingSamplingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // OPERATION_SAMPLING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.operationSampling = new PerOperationSamplingStrategies();
              struct.operationSampling.read(iprot);
              struct.setOperationSamplingIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, SamplingStrategyResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.strategyType != null) {
        oprot.writeFieldBegin(STRATEGY_TYPE_FIELD_DESC);
        oprot.writeI32(struct.strategyType.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.probabilisticSampling != null) {
        if (struct.isSetProbabilisticSampling()) {
          oprot.writeFieldBegin(PROBABILISTIC_SAMPLING_FIELD_DESC);
          struct.probabilisticSampling.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.rateLimitingSampling != null) {
        if (struct.isSetRateLimitingSampling()) {
          oprot.writeFieldBegin(RATE_LIMITING_SAMPLING_FIELD_DESC);
          struct.rateLimitingSampling.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.operationSampling != null) {
        if (struct.isSetOperationSampling()) {
          oprot.writeFieldBegin(OPERATION_SAMPLING_FIELD_DESC);
          struct.operationSampling.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SamplingStrategyResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SamplingStrategyResponseTupleScheme getScheme() {
      return new SamplingStrategyResponseTupleScheme();
    }
  }

  private static class SamplingStrategyResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<SamplingStrategyResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SamplingStrategyResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI32(struct.strategyType.getValue());
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetProbabilisticSampling()) {
        optionals.set(0);
      }
      if (struct.isSetRateLimitingSampling()) {
        optionals.set(1);
      }
      if (struct.isSetOperationSampling()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetProbabilisticSampling()) {
        struct.probabilisticSampling.write(oprot);
      }
      if (struct.isSetRateLimitingSampling()) {
        struct.rateLimitingSampling.write(oprot);
      }
      if (struct.isSetOperationSampling()) {
        struct.operationSampling.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SamplingStrategyResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.strategyType = SamplingStrategyType.findByValue(iprot.readI32());
      struct.setStrategyTypeIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.probabilisticSampling = new ProbabilisticSamplingStrategy();
        struct.probabilisticSampling.read(iprot);
        struct.setProbabilisticSamplingIsSet(true);
      }
      if (incoming.get(1)) {
        struct.rateLimitingSampling = new RateLimitingSamplingStrategy();
        struct.rateLimitingSampling.read(iprot);
        struct.setRateLimitingSamplingIsSet(true);
      }
      if (incoming.get(2)) {
        struct.operationSampling = new PerOperationSamplingStrategies();
        struct.operationSampling.read(iprot);
        struct.setOperationSamplingIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

