// Copyright (C) 2020  Egon Willighagen
// License: MIT

import groovy.json.JsonSlurper

templateFile = "gene_database/template.md"

def createBioSchemas(file, type) {
  content = "<script type=\"application/ld+json\">{"
  content += "\"@context\": \"https://schema.org/\","
  content += "\"@type\": \"Dataset\","
  content += "\"name\": \"${file.file}\","
  content += "\"description\": \"BridgeDb identifier mapping file for ${file[type]}\","
  content += "\"identifier\": \"${file.doi}\","
  content += "\"keywords\": \"BridgeDb, mapping file, identifier, ${file.type}\","
  content += "\"url\": \"${file.downloadURL}\""
  content += "}</script> "
  return content
}

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
      print "| "
      print createBioSchemas(file, data.type.toLowerCase())
      print "${file[data.type.toLowerCase()]} "
      print "| [${file.file}](${file.downloadURL}) "
      print "| (doi:[${file.doi}](https://doi.org/${file.doi})) "
      println "|"
    }
  } else {
    println line
  }
}

