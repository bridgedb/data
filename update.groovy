// Copyright (C) 2020  Egon Willighagen
// License: MIT

import groovy.json.JsonSlurper

templateFile = "gene_database/template.md"

lines = new File(templateFile).readLines()
lines.each { String line ->
  if (line.startsWith("<files>")) {
    def instruction = new XmlSlurper().parseText(line)
    def input = instruction.text()    
    def jsonSlurper = new JsonSlurper()
    fileContents = new File(input).text
    def data = jsonSlurper.parseText(fileContents)
    println "| ${data.type} | BridgeDb Download | DOI |"
    println "|-------|--------|---------|"
    for (file in data.mappingFiles) {
      print "| ${file[data.type.toLowerCase()]} "
      print "| [${file.file}](${file.downloadURL}) "
      print "| (doi:[${file.doi}](https://doi.org/${file.doi})) "
      println "|"
    }
  } else {
    println line
  }
}

