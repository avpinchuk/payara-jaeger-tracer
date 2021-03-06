/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.github.avpinchuk.jaeger.thriftjava;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class BaggageRestriction implements org.apache.thrift.TBase<BaggageRestriction, BaggageRestriction._Fields>, java.io.Serializable, Cloneable, Comparable<BaggageRestriction> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("BaggageRestriction");

  private static final org.apache.thrift.protocol.TField BAGGAGE_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("baggageKey", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField MAX_VALUE_LENGTH_FIELD_DESC = new org.apache.thrift.protocol.TField("maxValueLength", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new BaggageRestrictionStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new BaggageRestrictionTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String baggageKey; // required
  public int maxValueLength; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    BAGGAGE_KEY((short)1, "baggageKey"),
    MAX_VALUE_LENGTH((short)2, "maxValueLength");

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
        case 1: // BAGGAGE_KEY
          return BAGGAGE_KEY;
        case 2: // MAX_VALUE_LENGTH
          return MAX_VALUE_LENGTH;
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
  private static final int __MAXVALUELENGTH_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.BAGGAGE_KEY, new org.apache.thrift.meta_data.FieldMetaData("baggageKey", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MAX_VALUE_LENGTH, new org.apache.thrift.meta_data.FieldMetaData("maxValueLength", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(BaggageRestriction.class, metaDataMap);
  }

  public BaggageRestriction() {
  }

  public BaggageRestriction(
    java.lang.String baggageKey,
    int maxValueLength)
  {
    this();
    this.baggageKey = baggageKey;
    this.maxValueLength = maxValueLength;
    setMaxValueLengthIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public BaggageRestriction(BaggageRestriction other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetBaggageKey()) {
      this.baggageKey = other.baggageKey;
    }
    this.maxValueLength = other.maxValueLength;
  }

  public BaggageRestriction deepCopy() {
    return new BaggageRestriction(this);
  }

  @Override
  public void clear() {
    this.baggageKey = null;
    setMaxValueLengthIsSet(false);
    this.maxValueLength = 0;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getBaggageKey() {
    return this.baggageKey;
  }

  public BaggageRestriction setBaggageKey(@org.apache.thrift.annotation.Nullable java.lang.String baggageKey) {
    this.baggageKey = baggageKey;
    return this;
  }

  public void unsetBaggageKey() {
    this.baggageKey = null;
  }

  /** Returns true if field baggageKey is set (has been assigned a value) and false otherwise */
  public boolean isSetBaggageKey() {
    return this.baggageKey != null;
  }

  public void setBaggageKeyIsSet(boolean value) {
    if (!value) {
      this.baggageKey = null;
    }
  }

  public int getMaxValueLength() {
    return this.maxValueLength;
  }

  public BaggageRestriction setMaxValueLength(int maxValueLength) {
    this.maxValueLength = maxValueLength;
    setMaxValueLengthIsSet(true);
    return this;
  }

  public void unsetMaxValueLength() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __MAXVALUELENGTH_ISSET_ID);
  }

  /** Returns true if field maxValueLength is set (has been assigned a value) and false otherwise */
  public boolean isSetMaxValueLength() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __MAXVALUELENGTH_ISSET_ID);
  }

  public void setMaxValueLengthIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __MAXVALUELENGTH_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case BAGGAGE_KEY:
      if (value == null) {
        unsetBaggageKey();
      } else {
        setBaggageKey((java.lang.String)value);
      }
      break;

    case MAX_VALUE_LENGTH:
      if (value == null) {
        unsetMaxValueLength();
      } else {
        setMaxValueLength((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case BAGGAGE_KEY:
      return getBaggageKey();

    case MAX_VALUE_LENGTH:
      return getMaxValueLength();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case BAGGAGE_KEY:
      return isSetBaggageKey();
    case MAX_VALUE_LENGTH:
      return isSetMaxValueLength();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof BaggageRestriction)
      return this.equals((BaggageRestriction)that);
    return false;
  }

  public boolean equals(BaggageRestriction that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_baggageKey = true && this.isSetBaggageKey();
    boolean that_present_baggageKey = true && that.isSetBaggageKey();
    if (this_present_baggageKey || that_present_baggageKey) {
      if (!(this_present_baggageKey && that_present_baggageKey))
        return false;
      if (!this.baggageKey.equals(that.baggageKey))
        return false;
    }

    boolean this_present_maxValueLength = true;
    boolean that_present_maxValueLength = true;
    if (this_present_maxValueLength || that_present_maxValueLength) {
      if (!(this_present_maxValueLength && that_present_maxValueLength))
        return false;
      if (this.maxValueLength != that.maxValueLength)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetBaggageKey()) ? 131071 : 524287);
    if (isSetBaggageKey())
      hashCode = hashCode * 8191 + baggageKey.hashCode();

    hashCode = hashCode * 8191 + maxValueLength;

    return hashCode;
  }

  @Override
  public int compareTo(BaggageRestriction other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetBaggageKey()).compareTo(other.isSetBaggageKey());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBaggageKey()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.baggageKey, other.baggageKey);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMaxValueLength()).compareTo(other.isSetMaxValueLength());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaxValueLength()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maxValueLength, other.maxValueLength);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("BaggageRestriction(");
    boolean first = true;

    sb.append("baggageKey:");
    if (this.baggageKey == null) {
      sb.append("null");
    } else {
      sb.append(this.baggageKey);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("maxValueLength:");
    sb.append(this.maxValueLength);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (baggageKey == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'baggageKey' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'maxValueLength' because it's a primitive and you chose the non-beans generator.
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

  private static class BaggageRestrictionStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public BaggageRestrictionStandardScheme getScheme() {
      return new BaggageRestrictionStandardScheme();
    }
  }

  private static class BaggageRestrictionStandardScheme extends org.apache.thrift.scheme.StandardScheme<BaggageRestriction> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, BaggageRestriction struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // BAGGAGE_KEY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.baggageKey = iprot.readString();
              struct.setBaggageKeyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MAX_VALUE_LENGTH
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.maxValueLength = iprot.readI32();
              struct.setMaxValueLengthIsSet(true);
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
      if (!struct.isSetMaxValueLength()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'maxValueLength' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, BaggageRestriction struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.baggageKey != null) {
        oprot.writeFieldBegin(BAGGAGE_KEY_FIELD_DESC);
        oprot.writeString(struct.baggageKey);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(MAX_VALUE_LENGTH_FIELD_DESC);
      oprot.writeI32(struct.maxValueLength);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class BaggageRestrictionTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public BaggageRestrictionTupleScheme getScheme() {
      return new BaggageRestrictionTupleScheme();
    }
  }

  private static class BaggageRestrictionTupleScheme extends org.apache.thrift.scheme.TupleScheme<BaggageRestriction> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, BaggageRestriction struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.baggageKey);
      oprot.writeI32(struct.maxValueLength);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, BaggageRestriction struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.baggageKey = iprot.readString();
      struct.setBaggageKeyIsSet(true);
      struct.maxValueLength = iprot.readI32();
      struct.setMaxValueLengthIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

