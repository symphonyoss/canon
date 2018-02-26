<#macro printSchema context indent>
  <#if context.object>
    <@printSchema2 context indent true/> 
  <#elseif context.array>
    <@printSchema2 context indent false/>
  <#else>
    ERROR context is neither an object or array
  </#if>
</#macro>

<#macro printSchema2 context indent doName>
  <#if doName>
    <#local name="\"" + context.name + "\": ">
  <#else>
    <#local name="">
  </#if>
  <#local indent2=indent + "  ">
<#if context.textual>${indent}${name}"${context.text}"<#elseif context.number>${indent}${name}${context.text}<#elseif context.boolean>${indent}${name}${context.text}<#elseif context.object>${indent}${name}{
    <#list context.children as child>
<@printSchema2 child indent2 true/><#sep>,</#sep>
</#list>
${indent}}<#elseif context.array>${indent}${name}[
    <#list context.children as child>
<@printSchema2 child indent2 false/><#sep>,</#sep>
</#list>
${indent}]<#else>
    ERROR UNKNOWN CONTEXT TYPE ${context}
  </#if>
</#macro>
{
  "openapi": "3.0.0",
<@printSchema model.infoContext "  "/>,
  "servers": [
    {
      "url": "http://petstore.swagger.io/v1"
    }
  ],
  "paths": {
    "/pets": {
      "get": {
        "summary": "List all pets",
        "operationId": "listPets",
        "tags": [
          "pets"
        ],
        "parameters": [
          {
            "name": "limit",
            "in": "query",
            "description": "How many items to return at one time (max 100)",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "An paged array of pets",
            "headers": {
              "x-next": {
                "description": "A link to the next page of responses",
                "schema": {
                  "type": "string"
                }
              }
            },
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/Pets"
                }
              }
            }
          },
          "default": {
            "description": "unexpected error",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/Error"
                }
              }
            }
          }
        }
      },
      "post": {
        "summary": "Create a pet",
        "operationId": "createPets",
        "tags": [
          "pets"
        ],
        "responses": {
          "201": {
            "description": "Null response"
          },
          "default": {
            "description": "unexpected error",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/Error"
                }
              }
            }
          }
        }
      }
    },
    "/pets/{petId}": {
      "get": {
        "summary": "Info for a specific pet",
        "operationId": "showPetById",
        "tags": [
          "pets"
        ],
        "parameters": [
          {
            "name": "petId",
            "in": "path",
            "required": true,
            "description": "The id of the pet to retrieve",
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Expected response to a valid request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/Pets"
                }
              }
            }
          },
          "default": {
            "description": "unexpected error",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/Error"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
<#if model.components.schemaModels??>
<#list model.components.schemaModels.children as object>
<@printSchema object.context "      "/><#sep>,</#sep>
</#list>
<#else>
NO SCHEMAS!
</#if>
  }
}