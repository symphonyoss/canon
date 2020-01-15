---
nav_order: 1
title: Home
---
# Canon
Canon is a code generation framework which is very closely aligned with 
[OpenAPI 3](https://swagger.io/docs/specification/about/).
It is intended to generate code to represent data structures in an application domain, as well as REST APIs which use
those data structures.

Canon is a schema oriented framework which generates
implementation stubs for data structures as well as client and server API stubs
from a hand written and maintained schema. The generated code can be safely extended or enhanced with hand written code through
the use of "facade classes".

OpenAPI, on the other hand, _is an API description format for REST APIs. An OpenAPI file allows you to describe your entire API, including
endpoints and operations, operation parameters, authentication methods, 
contact information, license, terms of use and other information_.

Canon and OpenAPI have slightly different objectives, which leads to a slightly different schema specification, however,
there is significant overlap between the two and the Canon Schema Specification deviates from the 
OpenAPI schema specification only where necessary. Anyone familiar with OpenAPI should have no difficulty in reading
and understanding a Canon specification.

Canon code generation is based on [Freemarker](https://freemarker.apache.org/) templates and is intended to support the
generation of code in any language. At this time generation templates are available only for Java.

The Canon engine is implemented in Java and is available as a [Maven](https://maven.apache.org/) plugin, the code generation
process can easily be integrated into the Maven build process, as illustrated in
[this example](https://github.com/symphonyoss/canon-example/blob/master/presence-client/pom.xml#L70).

## Differences Between Canon and OpenAPI 3
OpenAPI specifications can be written in YAML or JSON.
There is an approximate mapping between JSON and a subset of YAML but this is imprecise and YAML is,
in the experience of the author, tricky to use because whitespace can be syntactically significant.

In OpenAPI 3 data models are defined using the **Schema Object**, which is an extended subset of JSON Schema Specification Wright Draft 00.

Canon specifications are written in JSON and the Canon Specification is defined by a 
[JSON Schema](https://github.com/symphonyoss/canon/blob/master/canon-parser/src/main/resources/canon-schema-v1.json).
There are elements of the specification to enable the inclusion of comments and multi-line string values which
OpenAPI facilitates by allowing specifications to be written in YAML.

Although OpenAPI supports the "Design-First" methodology, it is also possible to generate an OpenAPI specification from 
an annotated implementation. Canon, on the other hand, is "Design-First" only, and the specification is optimised to support
powerful code generation capabilities, at the expense of disallowing the representation of some APIs which can be described
by OpenAPI 3.

While there are some valid OpenAPI specifications which cannot be represented as a Canon schema, any valid Canon schema could be
represented by an OpenAPI specification and it would be possible to generate such a specification if required.

Canon generated code allows for the safe addition of hand written code through the use of "facade classes" which are sub-classes
of the main generated class. This mechanism completely separates the hand maintained code from the generated code, ensuring
that the generator never overwrites hand written code, but allowing for the re-generation of the model without breaking
the hand written additions (except where the change to the model directly effects the hand written additions).

## Schema Differences
The following sections describe the differences between the Canon Schema Specification and an OpenAPI 3 schema,
which are illustrated by this schema:

```json
{
  "canon": "0.0.1",
  "info": {
    "title": "Canon Schema Features",
    "#": "This schema demonstrates Canon schema features which differ from OpenAPI3 (like this comment)",
    "#AnotherComment": [
      "Comments can be multi line",
      "like this."
    ],
    "license": {
      "name": "Apache2"
    }
  },
  "id": "org.symphonyoss.s2.canon.test.schema",
  "version": "1.0",
  "model": {
    "javaGenPackage": "org.symphonyoss.s2.canon.test.schema",
    "javaFacadePackage": "org.symphonyoss.s2.canon.test.schema.facade"
  },
  "components": {
    "schemas": {
      "ApplicationPayload": {
        "type": "object",
        "#facades": "Are enabled like this:",
        "facade": true,
        "builderFacade": true,
        "properties": {
          "distributionList": {
            "type": "array",
            "x-canon-cardinality": "SET",
            "items": {
              "$ref": "#/components/schemas/PodAndUserId"
            }
          }
        }
      },
      "#This object has been commented out: AbstractApplicationObjectPayload": {
        "type": "object",
        "facade": true,
        "extends": "#/components/schemas/ApplicationPayload",
        "builderFacade": true,
        "description": "Base type for header or encrypted payloads in the object store.",
        "properties": {
        }
      },
      "AbstractApplicationObjectPayload": {
        "#Comment": "This is a comment.",
        "type": "object",
        "#inheritance": "Is done like this:",
        "extends": "#/components/schemas/ApplicationPayload",
        "facade": true,
        "builderFacade": true,
        "description": [
          "Multi-line descriptions can be written like this",
          "OpenAPI does not support this."
          ],
        "properties": {
        }
      },
      "PodAndUserId": {
        "type": "integer",
        "format": "int64",
        "facade": true
      }
    }
  }
}
```

### Schema Specification Version
OpenAPI schemas have an attribute called **openapi** in the root object containing the semantic version of the OpenAPI
specification to which the schema conforms. Canon schemas have a **canon**  attribute for the same purpose.

### Schema ID
A Canon schema requires an attribute called **id** in the top level object which should contain
a globally unique ID for the model. In the example above the value **org.symphonyoss.s2.canon.test.schema**
is used. It is recommended that this value should follow Java package naming conventions as a way of
ensuring such uniqueness.

Serialised Canon objects contain a type identifier in an attribute called **_type** which is composed of
the model ID defined above concatenated with a dot and the name of the object defined in the schema.
For example, an instance of the **AbstractApplicationObjectPayload** object defined above might be serialised
as:

```json
{
  "_type": "org.symphonyoss.s2.canon.test.schema.AbstractApplicationObjectPayload",
  "distributionList": [
    1,
    2
  ]
}
```

This allows a receiver of such serialised objects to deserialise them without knowing in advance what
type each object will be.

### Disallowed Names
Properties of defined objects with names beginning with an underscore character or the string 
**canon** are not permitted, even though they would be allowed in an OpenAPI schema. This is
to avoid collisions with serialised meta-data such as the type idntifier described above, and
with method and variable names in generated code.

### Model Object
A top level object called **model** is allowed which can be used to define model global values
and options to control the code generation process.

The Java code generation templates use the attributes **javaGenPackage** and **javaFacadePackage** 
to define the package names for the generated code and the facade classes respectively.

### Comments
Any JSON attribute whose name begins with a hash character is regarded as a comment and is ignored by the Canon parser.
Whole sections of a schema can be commented out by inserting a # as the first character of a name such as in the case
of the first instance of the **AbstractApplicationObjectPayload** object in the example above.

In most other places where an attribute with a name starting with a # would be illegal in an OpenAPI schema,
comments may have a value which is a String or an array of Strings. This is illustrated in the **info** section of the
schema above.

### Multi Line Descriptions
The **description** attribute in OpenAPI takes a String value, in a Canon schema a **description** may also take an
array of Stings as a means of improving readability in the schema itself and the generated code.
This is illustrated in the second instance of the **AbstractApplicationObjectPayload** object in the example above, which 
generates this JavaDoc class comment:

```java
/**
 * Object ObjectSchema(AbstractApplicationObjectPayload)
 *
 * Multi-line descriptions can be written like this
 * OpenAPI does not support this.
 * Generated from ObjectSchema(AbstractApplicationObjectPayload) at #/components/schemas/AbstractApplicationObjectPayload
 */
```

### Inheritance
OpenAPI supports the **AllOf** construct to support composition, which is sometimes used as a 
substitute for inheritance. Unfortunately the intended semantics of inheritance cannot be reliably 
determined from use of this construct, and the support of true inheritance greatly increases
the power of Canon generated code, especially when it comes to hand written facade classes which
inherit from other classes which also have facades.

Canon schemas support single inheritance as a first class citizen via the **extends** attribute
which is illustrated in the **AbstractApplicationObjectPayload** object in the schema above

### Facade Generation
The **facade** and **builderFacade** attributes control the generation of facade classes.
If the **facade** attribute is present with the value **true** then a facade is generated,
this means that for the generated class a facade sub-class is required in the main
source tree (usually **src/main/java**) which must be maintained by hand. Canon generates
proforma implementations of facades containing all the boiler plate code as a developer
convenience, but once initialised, these need to be maintained like any other source file.

If facade generation is not selected then the facade class is still generated but it appears
in the generated code package and directory, which avoids cluttering up the main source folder
with a lot of boiler plate only classes where facades are not required.

In most cases it is sufficient to override methods only in the instance class for a type, but
in some cases it is desirable to override methods in the builder class as well. If the
 **builderFacade** attribute is present with the value **true** then the facade also includes
 a builder facade. 

### Array cardinality
JSON does not have any way to distinguish between arrays representing lists or sets, but in many cases a
list is intended to represent a set of some sort. Canon supports the **x-canon-cardinality** property
for array attributes which may take the value **SET** or **LIST** with **LIST** being the default.

With **LIST** cardinality, duplicate values are allowed and the Java generated classes include an
ImmutableList<> containing the values. 
With **SET** cardinality, duplicate values are NOT allowed, when serialized the values are
sorted into lexicographical order and the Java generated classes include an
ImmutableSet<> containing the values.

### Canon Canonical Form
This is not a schema difference, but when serialised, Canon objects are represented in a canonical form
such that two semantically identical objects will serialize to the same byte sequence. This is essential
for applications which use hashes for identifiers, including the primary use case for Canon.

