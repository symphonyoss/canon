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
from a hand written and maintained schema.
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
