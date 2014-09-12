# Using Electronic Course Reserves Unleashed!

The **ecru** data base contains records of two types:

*  course -- a description of a course offered in a particular term
*  reading -- corresponds to one bibliographic item placed on reserve for a particular course

These records are created by loading them into the solr database. A class, **LoadCsvData**, is provided for loading records in CSV format; 
you may choose to use this as a guide for creating a more specialized class -- perhaps one that runs off an internal reserves system.

Once loaded, the database may be queried by RESTful HTTP calls using one of the URL patterns described elsewhere.

 

All responses to RESTful HTTP calls to **ecru** are in the form of a JSON results list in the following format:

```
{
  "summary" : {
    "numFound" : 1, // total number of search results
    "nextUrl" : "",  // a fully-formed URL to the next set of results, if it exists
    "prevUrl" : "", // a fully-formed URL to the previous set of results, if it exists
    "query" : "", //any additional query parameters added to the convenience URL
    "rows" : 10,  // the number of results in THIS set
    "start" : 0  // the starting index into the total number of search results
  },
  "items" : [  // an array of course objects, reading objects, or both
  
   ]
 }
```


## The Course JSON Object

| Field name | Description | Format |
| ---------- | ----------- | ----------- |
| **id** | Unique identifier within the database| string|
| **type** | Always "course" | string|
| **index**| The position of the object within the total search results| number|
|**course** | Always true; provided as a convenience for javascript processing| boolean|
| **startDate** | The date the course starts | ISO 8601 combined date time format|
| **endDate** | The date the course ends | ISO 8601 combined date time format|
| **term** | The name of the Term the course is in *(e.g.: "Spring 2015")* | string |
|**division** | The division, department, school, faculty that offers the course | string |
|**catNo** | The catalog number of the course | string|
|**name** | The name of the course *(e.g.: "Introduction to the Internet")* | string
| **instructors** | The name(s) of the course instructors| Array of strings|
| **displayLabel** |The course information display; this may differ from the **name** *(e.g.: "MIS 101: Introduction to the Internet: a Basic Course")* | string |
| **readings**| If **true**, indicates that there are reading records associated with this course | boolean |
|**url**| The URL for the course's web page/site, if it exists| string |
|**readingsUrl**|The URL that will fetch the readings for this course|string|
| **updateTimeStamp**| when the record was last updated |ISO 8601 combined date time format|
|**reading**| always false |boolean|
|**order** | always 0 |number|
|**required**| always false|boolean|





## The Reading JSON Object
| Field name | Description | Format |
| ---------- | ----------- | ----------- |
| **id** | Unique identifier within the database| string|
| **type** | Always "course" | string|
| **index**| The position of the object within the total search results| number|
|**reading** | Always true; provided as a convenience for javascript processing| boolean|
| **startDate** | The date the associated course starts | ISO 8601 combined date time format|
| **endDate** | The date the associated course ends | ISO 8601 combined date time format|
| **term** | The name of the Term the associated course is in *(e.g.: "Spring 2015")* | string |
|**courseId**| Unique ID of the associated course | string|
|**courseUrl**| URL to **ecru**'s course record |string|
|**order**|If the instructor has created an order for the readings to be used. **-1** if unassigned|number|
|**annotation**|If the instructor has made a note *(e.g.: "Please read the first two chapters before Sep. 20")* |string|
| **title** | Title of the Reading (or book reading is in) | string|
| **authors** | List of authors | Array of strings|
|**digUrl**| If the reading is available as a digital resource on the web, the URL|string|
|**journal** | If **true**, part of a journal, otherwise is a book | boolean|
|**chapter** | Title of chapter | string|
| **editors** | Editors of the book | Array of strings|
| **status** | Status of the reserve, *(e.g.: "Available", "Being Processed")* | string|
| **required**| If **true**, the reading is required by the instructor| string|
| **library**| Identifier for the library holding the reserve | string |
|**system**| The system the bibliographic record for the reading is, if any|string|
|**sysId**| The bib ID within the **system** corresponding to the reading|string|
|**isbn**| The ISBN(s), if any|string|
|**issn**| The ISSN(s), if any |string|
|**doi**| The Document Object ID, if any|string|
|**pubmed**| The PUBMED ID, if any |string|
|**course**| always **false**|boolean|
| **updateTimeStamp**| when the record was last updated |ISO 8601 combined date time format|




