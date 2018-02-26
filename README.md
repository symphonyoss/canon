# Canon
JSON API Generation Tooling

The example project can be found at https://github.com/symphonyoss/canon-example
Canon is intended to work together with Fugue, see https://symphonyoss.github.io/fugue/

# Change Log

## Version 0.0.3 - 2018-02-23
### Optional Facade Generation
Facades are now generated in the template directory along with other generated code unless the specification
says that a facade is required. This has the effect that for types where you don't want or need a facade the
facade class will be automatically regenerated as needed and you don't need to care about it.

```
"components": {
    "schemas": {
      "IntTypedef": {
        "description": "An integer typedef.",
        "type": "integer"
      },
      "DoubleTypedef": {
        "description": "A double typedef with a facade.",
        "facade": true,
        "type": "number",
        "format": "double",
        "minimum": -765546546547723.03330025,
        "maximum": 7665465456464550000.00333025
      }
    }
  }
}
```

In the example above the generated files for these two typedefs will be:

```
canon-test/
├── pom.xml
├── src
│   └── main
│       └── canon
│           └── typeCheck.json
└── target
    ├── generated-sources
    │   ├── annotations
    │   └── java
    │       └── org
    │           └── symphonyoss
    │               └── s2
    │                   └── canon
    │                       └── test
    │                           └── typeCheck
    │                               └── DoubleTypedefTypeDef.java
    │                               ├── IntTypedef.java
    │                               └── IntTypedefTypeDef.java
    └── proforma-sources
        └── java
            └── org
                └── symphonyoss
                    └── s2
                        └── canon
                            └── test
                                └── typeCheck
                                    └── facade
                                        └── DoubleTypedef.java

```

Note that the facade for IntTypeDef appears in the generated-sources directory whereas the facade for DoubleTypeDef appears in proforma-sources and should be checked in and maintained as a source file. The line in the specification which causes this is

```
"facade": true,
```

Note that the default for this value is false.

### Inheritance
It is now possible to define (single) inheritance relationships between object types. Consider this specification:

```
"components": {
    "schemas": {
"FundamentalObject": {
        "type": "object",
        "facade": true,
        "required": [
          "absoluteHash"
        ],
        "properties": {
          "absoluteHash": {
            "$ref": "#/components/schemas/DirectHash"
          },
          "sequenceHashes": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/DirectHash"
            }
          }
        }
      },
      
      "VersionedObject": {
        "type": "object",
        "facade": true,
        "extends": "#/components/schemas/FundamentalObject",
        "required": [
          "prevHash",
          "baseHash"
        ],
        "properties": {
          "prevHash": {
            "$ref": "#/components/schemas/DirectHash"
          },
          "baseHash": {
            "$ref": "#/components/schemas/DirectHash"
          }
        }
      }
    }
  }
}
```

The declaration **"extends": "#/components/schemas/FundamentalObject",** in VersionedObject makes this a sub-class of FundamentalObject.

Note that both of these objects are declared to have a developer managed facade.

The class hierarchy generated is:

VersionedObject _extends_ VersionedObjectEntity _extends_ FundamentalObject _extends_ FundamentalObjectEntity

Where VersionedObjectEntity is the generated super-class and VersionedObject is the developer maintained facade. This means that subclasses inherit the developer maintained additions to any super classes.
