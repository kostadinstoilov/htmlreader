# HTML Reader

This is a simple Java tool which reads people's first and last names from HTML tables in the following format:

```html
<table>
<tr>...<th>First Name</th><th>Last Name</th>...</tr>
<tr>...<td>Kostadin</td><td>Stoilov</td>...</tr>
```
The tool produces output in either plain (FirstName LastName) or RDF.(n-triples, turtle, rdfxml) format.

There are two files with sample data in this repository with which the tool is tested, but theoretically it should work with any HTML table in the above format.

## Setup

This project requires that you have maven installed and present in your $PATH. See - https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html

```bash
git clone https://github.com/kostadinstoilov/htmlreader.git
cd htmlreader
mvn compile
```

## Usage

Use the supplied A.html and B.html files with plain output:

```bash
mvn exec:java -Dexec.mainClass="com.ontotext.kstoilov.interview.task1.Task1" -Dexec.args="A.html B.html"
```

The tool will dump output in A.html.plain, B.html.plain.

Use the same files from the web with plain output:

```bash
mvn exec:java -Dexec.mainClass="com.ontotext.kstoilov.interview.task1.Task1" -Dexec.args="http://store4.data.bg/ruin/tasks/A.html http://store4.data.bg/ruin/tasks/B.html"
```

Use the supplied A.html and B.html files with ```rdf.xml``` output:

```bash
mvn exec:java -Dexec.mainClass="com.ontotext.kstoilov.interview.task1.Task1" -Dexec.args="A.html B.html rdf.xml"
```

The tool will dump output in A.html.rdf.xml, B.html.rdf.xml.


## Supported RDF output options (Argument #3)

```
rdf.xml
rdf.ntriples
rdf.turtle
```




