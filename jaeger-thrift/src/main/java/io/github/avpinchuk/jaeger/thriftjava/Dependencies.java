/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.github.avpinchuk.jaeger.thriftjava;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public class Dependencies implements org.apache.thrift.TBase<Dependencies, Dependencies._Fields>, java.io.Serializable, Cloneable, Comparable<Dependencies> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Dependencies");

  private static final org.apache.thrift.protocol.TField LINKS_FIELD_DESC = new org.apache.thrift.protocol.TField("links", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DependenciesStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DependenciesTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.util.List<DependencyLink> links; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LINKS((short)1, "links");

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
        case 1: // LINKS
          return LINKS;
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
    tmpMap.put(_Fields.LINKS, new org.apache.thrift.meta_data.FieldMetaData("links", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DependencyLink.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Dependencies.class, metaDataMap);
  }

  public Dependencies() {
  }

  public Dependencies(
    java.util.List<DependencyLink> links)
  {
    this();
    this.links = links;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Dependencies(Dependencies other) {
    if (other.isSetLinks()) {
      java.util.List<DependencyLink> __this__links = new java.util.ArrayList<DependencyLink>(other.links.size());
      for (DependencyLink other_element : other.links) {
        __this__links.add(new DependencyLink(other_element));
      }
      this.links = __this__links;
    }
  }

  public Dependencies deepCopy() {
    return new Dependencies(this);
  }

  @Override
  public void clear() {
    this.links = null;
  }

  public int getLinksSize() {
    return (this.links == null) ? 0 : this.links.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<DependencyLink> getLinksIterator() {
    return (this.links == null) ? null : this.links.iterator();
  }

  public void addToLinks(DependencyLink elem) {
    if (this.links == null) {
      this.links = new java.util.ArrayList<DependencyLink>();
    }
    this.links.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<DependencyLink> getLinks() {
    return this.links;
  }

  public Dependencies setLinks(@org.apache.thrift.annotation.Nullable java.util.List<DependencyLink> links) {
    this.links = links;
    return this;
  }

  public void unsetLinks() {
    this.links = null;
  }

  /** Returns true if field links is set (has been assigned a value) and false otherwise */
  public boolean isSetLinks() {
    return this.links != null;
  }

  public void setLinksIsSet(boolean value) {
    if (!value) {
      this.links = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case LINKS:
      if (value == null) {
        unsetLinks();
      } else {
        setLinks((java.util.List<DependencyLink>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case LINKS:
      return getLinks();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case LINKS:
      return isSetLinks();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Dependencies)
      return this.equals((Dependencies)that);
    return false;
  }

  public boolean equals(Dependencies that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_links = true && this.isSetLinks();
    boolean that_present_links = true && that.isSetLinks();
    if (this_present_links || that_present_links) {
      if (!(this_present_links && that_present_links))
        return false;
      if (!this.links.equals(that.links))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetLinks()) ? 131071 : 524287);
    if (isSetLinks())
      hashCode = hashCode * 8191 + links.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Dependencies other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetLinks()).compareTo(other.isSetLinks());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLinks()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.links, other.links);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Dependencies(");
    boolean first = true;

    sb.append("links:");
    if (this.links == null) {
      sb.append("null");
    } else {
      sb.append(this.links);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (links == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'links' was not present! Struct: " + toString());
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

  private static class DependenciesStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DependenciesStandardScheme getScheme() {
      return new DependenciesStandardScheme();
    }
  }

  private static class DependenciesStandardScheme extends org.apache.thrift.scheme.StandardScheme<Dependencies> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Dependencies struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LINKS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.links = new java.util.ArrayList<DependencyLink>(_list0.size);
                @org.apache.thrift.annotation.Nullable DependencyLink _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = new DependencyLink();
                  _elem1.read(iprot);
                  struct.links.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setLinksIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Dependencies struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.links != null) {
        oprot.writeFieldBegin(LINKS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.links.size()));
          for (DependencyLink _iter3 : struct.links)
          {
            _iter3.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DependenciesTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DependenciesTupleScheme getScheme() {
      return new DependenciesTupleScheme();
    }
  }

  private static class DependenciesTupleScheme extends org.apache.thrift.scheme.TupleScheme<Dependencies> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Dependencies struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.links.size());
        for (DependencyLink _iter4 : struct.links)
        {
          _iter4.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Dependencies struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.links = new java.util.ArrayList<DependencyLink>(_list5.size);
        @org.apache.thrift.annotation.Nullable DependencyLink _elem6;
        for (int _i7 = 0; _i7 < _list5.size; ++_i7)
        {
          _elem6 = new DependencyLink();
          _elem6.read(iprot);
          struct.links.add(_elem6);
        }
      }
      struct.setLinksIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
