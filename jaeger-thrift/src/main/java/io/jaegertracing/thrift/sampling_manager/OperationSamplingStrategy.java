/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.jaegertracing.thrift.sampling_manager;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class OperationSamplingStrategy implements org.apache.thrift.TBase<OperationSamplingStrategy, OperationSamplingStrategy._Fields>, java.io.Serializable, Cloneable, Comparable<OperationSamplingStrategy> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OperationSamplingStrategy");

  private static final org.apache.thrift.protocol.TField OPERATION_FIELD_DESC = new org.apache.thrift.protocol.TField("operation", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PROBABILISTIC_SAMPLING_FIELD_DESC = new org.apache.thrift.protocol.TField("probabilisticSampling", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new OperationSamplingStrategyStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new OperationSamplingStrategyTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String operation; // required
  public @org.apache.thrift.annotation.Nullable ProbabilisticSamplingStrategy probabilisticSampling; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    OPERATION((short)1, "operation"),
    PROBABILISTIC_SAMPLING((short)2, "probabilisticSampling");

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
        case 1: // OPERATION
          return OPERATION;
        case 2: // PROBABILISTIC_SAMPLING
          return PROBABILISTIC_SAMPLING;
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
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.OPERATION, new org.apache.thrift.meta_data.FieldMetaData("operation", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PROBABILISTIC_SAMPLING, new org.apache.thrift.meta_data.FieldMetaData("probabilisticSampling", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ProbabilisticSamplingStrategy.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OperationSamplingStrategy.class, metaDataMap);
  }

  public OperationSamplingStrategy() {
  }

  public OperationSamplingStrategy(
    java.lang.String operation,
    ProbabilisticSamplingStrategy probabilisticSampling)
  {
    this();
    this.operation = operation;
    this.probabilisticSampling = probabilisticSampling;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OperationSamplingStrategy(OperationSamplingStrategy other) {
    if (other.isSetOperation()) {
      this.operation = other.operation;
    }
    if (other.isSetProbabilisticSampling()) {
      this.probabilisticSampling = new ProbabilisticSamplingStrategy(other.probabilisticSampling);
    }
  }

  public OperationSamplingStrategy deepCopy() {
    return new OperationSamplingStrategy(this);
  }

  @Override
  public void clear() {
    this.operation = null;
    this.probabilisticSampling = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getOperation() {
    return this.operation;
  }

  public OperationSamplingStrategy setOperation(@org.apache.thrift.annotation.Nullable java.lang.String operation) {
    this.operation = operation;
    return this;
  }

  public void unsetOperation() {
    this.operation = null;
  }

  /** Returns true if field operation is set (has been assigned a value) and false otherwise */
  public boolean isSetOperation() {
    return this.operation != null;
  }

  public void setOperationIsSet(boolean value) {
    if (!value) {
      this.operation = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public ProbabilisticSamplingStrategy getProbabilisticSampling() {
    return this.probabilisticSampling;
  }

  public OperationSamplingStrategy setProbabilisticSampling(@org.apache.thrift.annotation.Nullable ProbabilisticSamplingStrategy probabilisticSampling) {
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

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case OPERATION:
      if (value == null) {
        unsetOperation();
      } else {
        setOperation((java.lang.String)value);
      }
      break;

    case PROBABILISTIC_SAMPLING:
      if (value == null) {
        unsetProbabilisticSampling();
      } else {
        setProbabilisticSampling((ProbabilisticSamplingStrategy)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case OPERATION:
      return getOperation();

    case PROBABILISTIC_SAMPLING:
      return getProbabilisticSampling();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case OPERATION:
      return isSetOperation();
    case PROBABILISTIC_SAMPLING:
      return isSetProbabilisticSampling();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof OperationSamplingStrategy)
      return this.equals((OperationSamplingStrategy)that);
    return false;
  }

  public boolean equals(OperationSamplingStrategy that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_operation = true && this.isSetOperation();
    boolean that_present_operation = true && that.isSetOperation();
    if (this_present_operation || that_present_operation) {
      if (!(this_present_operation && that_present_operation))
        return false;
      if (!this.operation.equals(that.operation))
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

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetOperation()) ? 131071 : 524287);
    if (isSetOperation())
      hashCode = hashCode * 8191 + operation.hashCode();

    hashCode = hashCode * 8191 + ((isSetProbabilisticSampling()) ? 131071 : 524287);
    if (isSetProbabilisticSampling())
      hashCode = hashCode * 8191 + probabilisticSampling.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(OperationSamplingStrategy other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetOperation()).compareTo(other.isSetOperation());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOperation()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.operation, other.operation);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("OperationSamplingStrategy(");
    boolean first = true;

    sb.append("operation:");
    if (this.operation == null) {
      sb.append("null");
    } else {
      sb.append(this.operation);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("probabilisticSampling:");
    if (this.probabilisticSampling == null) {
      sb.append("null");
    } else {
      sb.append(this.probabilisticSampling);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (operation == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'operation' was not present! Struct: " + toString());
    }
    if (probabilisticSampling == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'probabilisticSampling' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (probabilisticSampling != null) {
      probabilisticSampling.validate();
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

  private static class OperationSamplingStrategyStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OperationSamplingStrategyStandardScheme getScheme() {
      return new OperationSamplingStrategyStandardScheme();
    }
  }

  private static class OperationSamplingStrategyStandardScheme extends org.apache.thrift.scheme.StandardScheme<OperationSamplingStrategy> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OperationSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // OPERATION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.operation = iprot.readString();
              struct.setOperationIsSet(true);
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
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, OperationSamplingStrategy struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.operation != null) {
        oprot.writeFieldBegin(OPERATION_FIELD_DESC);
        oprot.writeString(struct.operation);
        oprot.writeFieldEnd();
      }
      if (struct.probabilisticSampling != null) {
        oprot.writeFieldBegin(PROBABILISTIC_SAMPLING_FIELD_DESC);
        struct.probabilisticSampling.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OperationSamplingStrategyTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OperationSamplingStrategyTupleScheme getScheme() {
      return new OperationSamplingStrategyTupleScheme();
    }
  }

  private static class OperationSamplingStrategyTupleScheme extends org.apache.thrift.scheme.TupleScheme<OperationSamplingStrategy> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OperationSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.operation);
      struct.probabilisticSampling.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OperationSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.operation = iprot.readString();
      struct.setOperationIsSet(true);
      struct.probabilisticSampling = new ProbabilisticSamplingStrategy();
      struct.probabilisticSampling.read(iprot);
      struct.setProbabilisticSamplingIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

