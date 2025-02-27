// Copyright (C) 2020-2023  Egon Willighagen
// License: MIT

// import groovy.xml.XmlSlurper
import groovy.json.JsonSlurper

templateFile = "template.md"

licenseNames = [
  "http://creativecommons.org/publicdomain/zero/1.0/" : "CC0"
]

tools = [
  "PV33" : [
    "name" : "PathVisio 3.3",
    "website" : "https://github.com/PathVisio/pathvisio/releases/tag/v3.3.0"
  ],
  "PV3.3" : [
    "name" : "PathVisio 3.3",
    "website" : "https://github.com/PathVisio/pathvisio/releases/tag/v3.3.0"
  ],
  "BioC" : [
    "name" : "BridgeDbR",
    "website" : "https://bioconductor.org/packages/release/bioc/html/BridgeDbR.html"
  ],
  "WS": [
    "name" : "BridgeDb Webservice",
    "website" : "https://github.com/bridgedb/BridgeDbWebservice"
  ]
]

def createBioSchemas(file, type) {
  content = "<script type=\"application/ld+json\">{"
  content += "\"@context\": \"https://schema.org/\","
  content += "\"@type\": \"Dataset\","
  content += "\"http://purl.org/dc/terms/conformsTo\": { \"@type\": \"CreativeWork\", \"@id\": \"https://bioschemas.org/profiles/Dataset/1.0-RELEASE\" },"
  content += "\"name\": \"${file.file}\","
  commonName = file.common != null ? ", " + file.common : ""
  extra = type.toLowerCase() == "species" ? " for genes and proteins" : " (species independent)"
  content += "\"description\": \"BridgeDb identifier mapping file for ${file[type.toLowerCase()]}${extra}\","
  content += "\"@id\": \"https://bridgedb.github.io/data/gene_database/${file.doi}/${file.file}\","
  content += "\"identifier\": \"${file.doi}/${file.file}\","
  extra = type.toLowerCase() == "species" ? file[type.toLowerCase()] + ", gene, protein" : type.toLowerCase()
  if (file.license) content += "\"license\": \"${file.license}\","
  content += "\"keywords\": \"BridgeDb, mapping file, identifier, ELIXIR RIR, ${extra}${commonName}\","
  content += "\"url\": \"https://doi.org/${file.doi}\","
  content += "\"distribution\": [ { \"@type\": \"DataDownload\", \"name\": \"$file.file\", \"contentURL\": \"${file.downloadURL}\" } ],"
  content += "\"isAccessibleForFree\": true"
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
    println "| ${data.type} | BridgeDb Download | QC report | Size | License | Date | Tested with"
    println "|-------|--------|---------|"
    for (file in data.mappingFiles) {
      print "| "
      print createBioSchemas(file, data.type)
      print "${file[data.type.toLowerCase()]} "
      typeID = file[data.type.toLowerCase()+"ID"]
      print (typeID ? "(<a href=\"https://bioregistry.io/${typeID}\">${typeID}</a>) " : " ")
      commonName = file["common"]
      print (commonName ? "(${commonName}) " : " ")
      print "| [${file.file}](${file.downloadURL}) (doi:[${file.doi}](https://doi.org/${file.doi})) "
      print "| " + (file.QCreport ? "[QC](${file.QCreport})" : "")
      print "| " + (file.size ? file.size : "")
      licenseStr = ""
      if (file.license) {
        licenseStr = "[" +
          (licenseNames[file.license] ?
            licenseNames[file.license] :
            "license") +
          "](" + file.license + ") "
      }
      print "| ${licenseStr}"
      dateStr = ""
      if (file.date) dateStr = file.date
      print "| ${dateStr} "
      testedWithStr = ""
      if (file.tested) {
        for (tool in file.tested) {
          testedWithStr += "<a href=\"${tools[tool].website}\">" + tools[tool].name + "</a> "
        }
      }
      print "| ${testedWithStr}"
      println "|"
    }
  } else {
    println line
  }
}

