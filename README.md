# data

Repository to mimic the old `data/gene_database/` API, available at [https://bridgedb.org/data/gene_database/](https://bridgedb.org/data/gene_database/).

The input is a JSON file from which the `gene_database/readme.md` is autogenerated
with the scripts in this folder:

```shell
groovy update.groovy > docs/gene_database/readme.md
```

A second script generated `gdb.config` content for BridgeDb servers:

```shell
groovy makeGDBconfig.groovy
```

This conversion is automatically [done by GitHub Actions](https://github.com/bridgedb/data/actions). The same workflow also triggers the [build and push workflow for the BridgeDB docker image](https://github.com/bridgedb/docker/actions/workflows/buildandpush.yml).

## data model

The data model for each identifier mapping file looks like this:

```json
{
      "species": "Human Coronaviruses",
      "file":    "humancorona-2021-11-27.bridge",
      "downloadURL": "https://zenodo.org/record/5734440/files/humancorona-2021-11-27.bridge?download=1",
      "doi": "10.5281/zenodo.5734440",
      "license": "http://creativecommons.org/publicdomain/zero/1.0/",
      "date": "2021-11-27",
      "size": "2.1 MB"
}
```

The `species` field is not always a species, like in the above example or for metabolites. The `file` and `size` fields
are the filename and exact size of the mapping file, and the `downloadURL` the exact URL to directly download
the Derby file ready for use. The `doi` and `license` field is the DOI and (open) license associated with the
data release.

For the `date` field, this is the data when the data was released. This could be the date when the archive was
released (so, the release date of the DOI record) or the data when the original data from which the Derby file
was created was released. For example, it could be the Ensembl release date for gene/protein mapping data.

## HTML page

The HTML page hosted by GitHub Pages is in this repository stored as a Markdown file, which is created from
the JSON files. The Markdown contains [Bioschemas annotation](https://bioschemas.org/) for the datasets. This
annotation looks like this (created with the information in the JSON files):

```json
{
  "@context": "https://schema.org/",
  "@type": "Dataset",
  "http://purl.org/dc/terms/conformsTo": {
    "@type": "CreativeWork",
    "@id": "https://bioschemas.org/profiles/Dataset/0.4-DRAFT/"
  },
  "name": "Ag_Derby_Ensembl_Metazoa_39.bridge",
  "description": "BridgeDb identifier mapping file for Anopheles gambiae for genes and proteins",
  "identifier": "10.5281/zenodo.3667670/Ag_Derby_Ensembl_Metazoa_39.bridge",
  "license": "https://zenodo.org/record/3667670/files/LICENSE?download=1",
  "keywords": "BridgeDb, mapping file, identifier, ELIXIR RIR, Anopheles gambiae, gene, protein",
  "url": "https://doi.org/10.5281/zenodo.3667670",
  "distribution": [
    {
      "@type": "DataDownload",
      "name": "Ag_Derby_Ensembl_Metazoa_39.bridge",
      "contentURL": "https://zenodo.org/record/3667670/files/Ag_Derby_Ensembl_Metazoa_39.bridge?download=1"
    }
  ],
  "isAccessibleForFree": true
}
```
