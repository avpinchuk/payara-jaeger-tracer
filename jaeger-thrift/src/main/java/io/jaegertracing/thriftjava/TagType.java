/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.jaegertracing.thriftjava;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2020-08-03")
public enum TagType implements org.apache.thrift.TEnum {
  STRING(0),
  DOUBLE(1),
  BOOL(2),
  LONG(3),
  BINARY(4);

  private final int value;

  private TagType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static TagType findByValue(int value) { 
    switch (value) {
      case 0:
        return STRING;
      case 1:
        return DOUBLE;
      case 2:
        return BOOL;
      case 3:
        return LONG;
      case 4:
        return BINARY;
      default:
        return null;
    }
  }
}
