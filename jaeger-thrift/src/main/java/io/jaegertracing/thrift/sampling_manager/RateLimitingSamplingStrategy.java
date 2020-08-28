/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.jaegertracing.thrift.sampling_manager;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class RateLimitingSamplingStrategy implements org.apache.thrift.TBase<RateLimitingSamplingStrategy, RateLimitingSamplingStrategy._Fields>, java.io.Serializable, Cloneable, Comparable<RateLimitingSamplingStrategy> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RateLimitingSamplingStrategy");

  private static final org.apache.thrift.protocol.TField MAX_TRACES_PER_SECOND_FIELD_DESC = new org.apache.thrift.protocol.TField("maxTracesPerSecond", org.apache.thrift.protocol.TType.I16, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new RateLimitingSamplingStrategyStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new RateLimitingSamplingStrategyTupleSchemeFactory();

  public short maxTracesPerSecond; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    MAX_TRACES_PER_SECOND((short)1, "maxTracesPerSecond");

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
        case 1: // MAX_TRACES_PER_SECOND
          return MAX_TRACES_PER_SECOND;
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
  private static final int __MAXTRACESPERSECOND_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.MAX_TRACES_PER_SECOND, new org.apache.thrift.meta_data.FieldMetaData("maxTracesPerSecond", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RateLimitingSamplingStrategy.class, metaDataMap);
  }

  public RateLimitingSamplingStrategy() {
  }

  public RateLimitingSamplingStrategy(
    short maxTracesPerSecond)
  {
    this();
    this.maxTracesPerSecond = maxTracesPerSecond;
    setMaxTracesPerSecondIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RateLimitingSamplingStrategy(RateLimitingSamplingStrategy other) {
    __isset_bitfield = other.__isset_bitfield;
    this.maxTracesPerSecond = other.maxTracesPerSecond;
  }

  public RateLimitingSamplingStrategy deepCopy() {
    return new RateLimitingSamplingStrategy(this);
  }

  @Override
  public void clear() {
    setMaxTracesPerSecondIsSet(false);
    this.maxTracesPerSecond = 0;
  }

  public short getMaxTracesPerSecond() {
    return this.maxTracesPerSecond;
  }

  public RateLimitingSamplingStrategy setMaxTracesPerSecond(short maxTracesPerSecond) {
    this.maxTracesPerSecond = maxTracesPerSecond;
    setMaxTracesPerSecondIsSet(true);
    return this;
  }

  public void unsetMaxTracesPerSecond() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __MAXTRACESPERSECOND_ISSET_ID);
  }

  /** Returns true if field maxTracesPerSecond is set (has been assigned a value) and false otherwise */
  public boolean isSetMaxTracesPerSecond() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __MAXTRACESPERSECOND_ISSET_ID);
  }

  public void setMaxTracesPerSecondIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __MAXTRACESPERSECOND_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case MAX_TRACES_PER_SECOND:
      if (value == null) {
        unsetMaxTracesPerSecond();
      } else {
        setMaxTracesPerSecond((java.lang.Short)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case MAX_TRACES_PER_SECOND:
      return getMaxTracesPerSecond();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case MAX_TRACES_PER_SECOND:
      return isSetMaxTracesPerSecond();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof RateLimitingSamplingStrategy)
      return this.equals((RateLimitingSamplingStrategy)that);
    return false;
  }

  public boolean equals(RateLimitingSamplingStrategy that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_maxTracesPerSecond = true;
    boolean that_present_maxTracesPerSecond = true;
    if (this_present_maxTracesPerSecond || that_present_maxTracesPerSecond) {
      if (!(this_present_maxTracesPerSecond && that_present_maxTracesPerSecond))
        return false;
      if (this.maxTracesPerSecond != that.maxTracesPerSecond)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + maxTracesPerSecond;

    return hashCode;
  }

  @Override
  public int compareTo(RateLimitingSamplingStrategy other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetMaxTracesPerSecond()).compareTo(other.isSetMaxTracesPerSecond());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaxTracesPerSecond()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maxTracesPerSecond, other.maxTracesPerSecond);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("RateLimitingSamplingStrategy(");
    boolean first = true;

    sb.append("maxTracesPerSecond:");
    sb.append(this.maxTracesPerSecond);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'maxTracesPerSecond' because it's a primitive and you chose the non-beans generator.
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RateLimitingSamplingStrategyStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RateLimitingSamplingStrategyStandardScheme getScheme() {
      return new RateLimitingSamplingStrategyStandardScheme();
    }
  }

  private static class RateLimitingSamplingStrategyStandardScheme extends org.apache.thrift.scheme.StandardScheme<RateLimitingSamplingStrategy> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RateLimitingSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // MAX_TRACES_PER_SECOND
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.maxTracesPerSecond = iprot.readI16();
              struct.setMaxTracesPerSecondIsSet(true);
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
      if (!struct.isSetMaxTracesPerSecond()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'maxTracesPerSecond' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, RateLimitingSamplingStrategy struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(MAX_TRACES_PER_SECOND_FIELD_DESC);
      oprot.writeI16(struct.maxTracesPerSecond);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RateLimitingSamplingStrategyTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RateLimitingSamplingStrategyTupleScheme getScheme() {
      return new RateLimitingSamplingStrategyTupleScheme();
    }
  }

  private static class RateLimitingSamplingStrategyTupleScheme extends org.apache.thrift.scheme.TupleScheme<RateLimitingSamplingStrategy> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RateLimitingSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI16(struct.maxTracesPerSecond);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RateLimitingSamplingStrategy struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.maxTracesPerSecond = iprot.readI16();
      struct.setMaxTracesPerSecondIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

