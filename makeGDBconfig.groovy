// Copyright (C) 2021-2023  Egon Willighagen
// License: MIT

import groovy.json.JsonSlurper

templateFile = "template.md"

// create the GDB config file content
lines = new File(templateFile).readLines()
lines.each { String line ->
  if (line.startsWith("<files>")) {
    def instruction = new XmlSlurper().parseText(line)
    def input = instruction.text()    
    def jsonSlurper = new JsonSlurper()
    fileContents = new File(input).text
    new File("/docs/${input}.config").withWriter('utf-8') { writer ->
      def data = jsonSlurper.parseText(fileContents)
      data.mappingFiles.each { mappingFile ->
        if (data.type == "Species") {
          if (mappingFile.species == "Human Coronaviruses") { // exception
            writer.writeLine "*\t${mappingFile.file}"
          } else {
            writer.writeLine "${mappingFile.species}\t${mappingFile.file}"
          }
        } else {
          writer.writeLine "*\t${mappingFile.file}"
        }
     }
    }
  }
}
