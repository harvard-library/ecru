
#ecru URL Patterns

The URL patterns for accessing the Course Reserves Unleashed! data  follow, to some extent, the guidelines laid out by Brain Mulloy in ["Web API Design"](http://pages.apigee.com/web-api-design-ebook.html) . 

## Base URL


The base URL of the API will be

```
http://{host}{port}/ecru/v2
```

The URL patterns presented below reflect the path portion that is appended to the base URL.



## Resource Types

There are two resource types. _With the exception of "search across all Resource Types" (see below),_  the resource type will be the first string in your path.

### courses

A _course_ resource refers to a specific course *instance*, that is, a course associated with a specific term .    For example,  _FAS-1256, Mathematics 19. Mathematical Modeling_ has been taught many times; a _course_ resource could be the Fall, 2013 instance of _FAS-1256, Mathematics 19. Mathematical Modeling_ .

The RESTful URL path to request data from the courses resource begins
```
/courses
```


### readings

A _reading_ is a specific journal or non-journal item that is associated with a specific _course_ instance.


The RESTful URL path to request data from the readings resource begins
```
/readings
```



## Searching

### Simple Search

A simple search against all text fields [sometimes known as a "Google-type search"] can be performed against either against the entire Course Reserves Unleashed database, or against  a specific resource.  In either case, the syntax is:
```
?q={your search terms here}
```


To perform a simple search against all resources, say, for "*Thomas*", the URL's path would be:
```
/all?q=thomas
```

This would return  all _course_ resources associated with those instructors, all _course_ resources that have *thomas* in the course title (e.g.: "The teachings of St. Thomas Aquinas" or where *thomas* was the first or last name of the instructor), and all _reading_ resources with *thomas* in the title or author name.

### More complex searches

Course Reserves Unleashed! is built on a [Solr 4](https://lucene.apache.org/solr/) index and database.  As such, any queries that are allowed with Solr 4 are supported. 

 Here is a complete list of fields that you can search on:


| Field name | Description | In Reading? | In Course? |
| ---------- | ----------- | ----------- | ---------- |
| _id_ | Unique identifier of the record | YES |YES |
| _type_| either "reading" or "course" | YES |YES |
| _term_ | the Term label (e.g.: "Fall 2013")| YES |YES |
| _start_date_ | Starting date of the Term | YES |YES |
| _end_date_ | Ending date of the Term | YES |YES |
| _updated_ | the date this record was updated | YES |YES |
| _course.name_ | the name of the course | NO | YES|
| _course.name_str_ | the course name as a literal |NO| YES|
| _course.division_ | For our purposes, the abbreviation of the "School" offering the course | NO | YES |
| _course.cat_no_ | The catalog number of the course | NO | YES |
| _course.instructor_ | Instructor; there may be more than one for a course| NO | YES |
| _reading.author_ |May be more than one | YES | NO |
| _reading.editor_ |May be more than one  | YES | NO |
| _reading.title_ | | YES | NO |
| _reading.chapter_ | | YES | NO |
| _reading.journal_ | | YES | NO |
| _reading.lecture_date_ | | YES | NO |
| _reading.status_ | "in process" or "available"| YES | NO |
| _reading.library_ | the Library abbreviation | YES | NO |
| _reading.system_ | "HVD01" or "HVD30" or empty | YES | NO |
| _reading.system_id_ | the Aleph number, if any | YES | NO |
| _reading.doi_ | | YES | NO |
| _reading.pubmed_ | | YES | NO |
| _reading.isbn_ | | YES | NO |
| _reading.issn_ | | YES | NO |
| _reading.required_ |true or false | YES | NO |
| _reading.course_id_ | The _id_ of the course instance requiring the reading | YES | NO |


Just for example, if you wanted all the current and future readings where **enlightenment bible**  was in the title, the path and query string would look like this:

```
/readings/terms/current?q=reading.title:"enlightenment bible"
```

### Faceting

Course Reserves Unleashed supports "faceted search":http://wiki.apache.org/solr/SolrFacetingOverview , although currently only on fields, as opposed to ranges or dates.

Here are fields you may want to facet on:

| Field name | Description | In Reading? | In Course? |
| ---------- | ----------- | ----------- | ---------- |
| _term_ | the Term label (e.g.: "Fall 2013")| YES |YES |
| _course.name_str_ | the course name as a literal |NO| YES|
| _course.division_ | For our purposes, the abbreviation of the "School" offering the course | NO | YES |
| _course.instructor_str_ | Instructor; there may be more than one for a course| NO | YES |
| _reading.author_str_ |May be more than one | YES | NO | 
| _reading.library_ | the Library abbreviation | YES | NO |

### Convenience APIs for Searching

Course Reserves Unleashed! has built in some convenience API paths.  Additional query parameters can be applied to these base API paths.

Some of these convenience paths provide automatic faceting as noted; you can add further **&facet.field** parameters to the query string, or suppress faceting (for a speeded-up search) by adding  **&facet=false** to the query string.

#### Against All Resources

* `/all/terms/current`  -- limits to all resources for all current and future Terms [facets: _course.division_, _reading.library_]
* `/all/terms/{term identifier}` -- limits to  all resources for the given Term.  "Wild carding" (E.g.: "*2013*" or "Fall *") is acceptable [facets: _course.division_, _reading.library_]


#### Against Course Resources

* `/courses/{course id}` --  limits to the Course Instance matching the input id [facets: course.division, term]
* `/courses/schools/{school abbreviation}` -- limits to the Course Instances associated with a particular school (FAS, DIV, etc.) facets:[term]
* `/courses/schools/{school abbreviation}/terms/current` -- further limits to the particular school **and** the Course Instances for current and future Terms
* `/courses/schools/{school abbreviation}/terms/{term identifier}` -- further limits  to the particular school **and** the specified Term.
* `/courses/terms/current` -- limits the search to the current and future Terms [ facets: course.division, term]
* `/courses/terms/{term identifier}` -- limits to the specified Term [ facets: course.division]

#### Against Reading Resources

* `/readings/{reading id}` -- limits to Reading matching the input id
* `/readings/courses/{course id}` -- limits to the Readings for the input course id
* `/readings/authors/{author name}` -- limits to all Readings for the input author
* `/readings/terms/current` -- limits to all Readings for current and future Terms
* `/readings/terms/{term identifier}` -- limits to all Readings for the specified Term
* `/readings/terms/current/libs/{library abbreviation}` -- limits to the current and future Terms **and** library
* `/readings/terms/current/libs/{library abbreviation}/authors/{author name}` -- further limits to the author
* `/readings/terms/{term identifier}/libs/{library abbreviation}/authors/{author name}` -- further limits to the author


