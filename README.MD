# universAAL IoT ontology code generator template
With this template the [code generator system](https://github.com/AIoTES/DevTools-IDE-CodeGenerator) can be configured to automatically generate fully functional, and as compliant as possible, universAAL ontology code from valid OWL ontology description.

With this template, estandard ontologies can be generated for universAAL to interpret, without having to manually recode these ontologies into universAAL IoT java code.
In the same way ontology design can be performed in OWL, and have the tool automatically generate the appropriate code.

The output of the tool is still universAAL IoT Java code, which needs to be compiled (which can be automated) and added as module to the universAAL IoT runtime. This tool is not meant for runtime interpretation of OWL files for universAAL.

## Getting started
The most useful way to use this template is to employ the maven plugin codegenerator, so ensure this is compiled first.

add the following to the pom.xml of the ontology project:
```xml
...
<build>
    <plugins>
      <plugin>
        <groupId>es.upm.tfo.lst</groupId>
        <artifactId>codegenerator.maven.plugin</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <executions>
          <execution>
            <id>generate-code</id>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
            <xmlTemplate>https://raw.githubusercontent.com/universAAL/uaal.ont.template/master/src/main/resources/compile.xml</xmlTemplate>
            <remoteOntologies> 
                <ontology>https://www.w3.org/ns/sosa/</ontology>
                <ontology>https://www.w3.org/ns/ssn/</ontology>
            </remoteOntologies>
        </configuration>
      </plugin>
    </plugins>
  </build>
...
```
This will generate a bundle with 2 ontologies (each in its own package, and each registered in the given order). To change the ontology to be transformed, change the url of the ontology/es you whish to tranform.

Please note that the code is generated during the "generate-source" maven phase, which means that if you run `mvn install` goal, the code is generated then compiled, tested and packaged as usual. It is recommended to configure your project as any stantard universAAL IoT ontology (i.e. artifactID, parent, dependencies, etc.), inclinding the standard tests to ensure the module will. 

## Advance features
With the code above OWL-imported ontologies will not be transformed, which may lead to "class not found" error when loading the ontology in runtime; to include ALL imported ontologies recursivelly use the follwing configuration. registration order of the ontologies will be automatically worked out.

```xml
...
<build>
    <plugins>
      <plugin>
        <groupId>es.upm.tfo.lst</groupId>
        <artifactId>codegenerator.maven.plugin</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <executions>
          <execution>
            <id>generate-code</id>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
            <xmlTemplate>https://raw.githubusercontent.com/universAAL/uaal.ont.template/master/src/main/resources/compile.xml</xmlTemplate>
            <recursiveOntologies>
                <ontology>https://www.w3.org/ns/sosa/</ontology>
            </recursiveOntologies>
        </configuration>
      </plugin>
    </plugins>
  </build>
...
```

Any and all owl files in `src/main/owl/` directory will be added as standalone ontologies. Each will be generated in its own package, and each registered in the alphabetical order, unless depenencies are detected. Imports will not be added recursivelly, to do so add your imports as `recursiveOntologies`.

All three methods may be combined, so you may add custom owl ontologies together with remote ontologies and some recursivelly added ontologies. This is useful for 

You may also add custom code to the final product (as usual maven java code), but this is an advance feature, ensure you understand the tool first.

### Generate a Maven Project
If you need to review the generated code you may look in the `target/generated-sources/owl-gen` folder. But if you need to edit this code there is another alternative. By using the `MavenProject.xml` template instead of the `compile.xml` template the output will be a complete maven project which you can then employ as any other universAAL IoT source code, including editing it. For this option it is best recommended to use either the Protègè codegenerator plugin or the REST codegenerator plugin, as the workflow is cleaner. In either case updates, i.e. generating the ontology over the code again, may not work as expected.

## Important
Never attempt to generate universAAL IoT core ontologies (i.e. mw.data* nor mw.bus.*), this will lead to "class already registered" errors. When generating an ontology that imports these, ensure the recursive function is not used, or if it is used it will not include these ontologies.

Case 1: generating a standalone owl file, all imports are standard universAAL IoT Ontologies. This will cause no problemas, the generated ontology will be loaded correctly as long as the dependencies and registration order in runtiume is correct. 

Case 2: generating a standalone owl file, imports include standard universAAL IoT Ontologies and other ontologies. You may add other ontologies with `remoteOntologies` (for non-recursive processing), recursiveOntologies` (for recursive processing); or, probably the prefered method, generate the ontologies in their own project-module, this will have the advantage that those representation can be used independently.

Case 3: generating a remote ontology where imports include standard universAAL IoT Ontologies; in this case never include the reference in `recursiveOntologies`. Add the reference as `remoteOntologies` treat the rest of the imports as in case 2.


## Contributing
Note these templates are using velocity template language to generate universAAL IoT java code, using the code-generator tool, you'll need to understand both. [Here](https://www.youtube.com/playlist?list=PL1oIPhHLHX26SQ14j4_ywcbFBFot2snyh) is a youtube course explaining the code-generator tool and the velocity template language.

Pull requests are always appreciated. 

## Credits

This software is manteined by: 
* Alejandro Medrano <amedrano@lst.tfo.upm.es> 
* Eduardo Bhuhid <ebuhid@lst.tfo.upm.es> 

The code-generator tool and this template have been partially funded by the [ACTIVAGE project](http://www.activageproject.eu/).

## Licence
```
   Copyright 2018 Universidad Politécnica de Madrid

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```