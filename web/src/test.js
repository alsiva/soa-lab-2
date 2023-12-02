// create a new XML document
var xmlDoc = document.implementation.createDocument(null, "books", null);

// create a new book element
var book1 = xmlDoc.createElement("book");

// set the ID attribute of the book element
book1.setAttribute("id", "1");

// create a new title element
var title1 = xmlDoc.createElement("title");

// create a new text node for the title element
var title1Text = xmlDoc.createTextNode("JavaScript: The Definitive Guide");

// append the text node to the title element
title1.appendChild(title1Text);

// create a new author element
var author1 = xmlDoc.createElement("author");

// create a new text node for the author element
var author1Text = xmlDoc.createTextNode("David Flanagan");

// append the text node to the author element
author1.appendChild(author1Text);

// append the title and author elements to the book element
book1.appendChild(title1);
book1.appendChild(author1);

// append the book element to the books element
xmlDoc.getElementsByTagName("books")[0].appendChild(book1);

// serialize the XML document into a string
var xmlString = new XMLSerializer().serializeToString(xmlDoc);

console.log(xmlString)