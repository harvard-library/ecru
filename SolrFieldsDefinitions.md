# Fields available through Ecru

Below is a table of the fields available from the Solr database available to Ecru.  All dates are in [ISO 8601 combined date time format](https://en.wikipedia.org/wiki/ISO_8601 "Link to wikipedia article").

Field Name | Indexed? | Stored? | Course? | Reading | Comment
:---------- | :--------: | :-------: | :-------: | ------- | ----
id | X | X | X | X |
type  | X | X | X | X | **"reading"** or  **"course"**
 display_label  |  | X |  X | X | Either course information or the bibliographic data of a reading
 updated  | X | X |  X | X |  Date last updated
 start_date  | X | X |  X | X | Course start date
 end_date  | X | X | X| | Course end date
 term  | X | X | X| X| Term name; e.g.: "Fall 2013"
 course.name  | X | X | X| |
 course.division  | X | X |  X|  |For Harvard, the Faculty/School abbreviation (e.g: HDS)
 course.cat_no  | X | X |  X|  | The catalogue number
 course.url  |  | X | X|  |
 course.instructor  | X | X | X | | Multiple field (Last,   First)
 course.has_readings  | X | X |X | | true or false
 reading.author  | X | X |  | X | Multiple field (Last,  First)
 reading.editor  | X | X |  | X |Multiple field (Last,  First)
 reading.first_author  | X |  | | X | Used for sorting by author
reading.title  | X | X |  | X |
 reading.chapter  | X | X |  | X |
 reading.journal  | X | X |  | X |
 reading.lecture_date  | X | X |  | X |
 reading.status  | X | X |  | X |For Harvard: available or in process
 reading.library  | X | X | | X | The library's abbreviation (e.g.: LAM)
 reading.system  | X | X | | X | For Harvard: HVD01 or HVD30
 reading.system_id  | X | X | | X | The bibliograpic item number within the system.
 reading.doi  | X | X |  | X |Document ID
 reading.pubmed  | X | X | | X | PubMed ID
 reading.isbn  | X | X |  | X |
 reading.issn  | X | X |  | X |
 reading.dig_url  |  | X | | X | Link to on-line copy
 reading.avail_url  |  | X |  | X |Link to Hollis' availability page
 reading.required  |  | X | | X | true or false
 reading.course_id  |  | X |  | X |
reading.order |  | X | | X | (default: -1) The order of the reading in the list of readings
reading.annotation |  | X | | X | A note to the student
course.name_str   | X |  | X| | Used for sorting/faceting by course name
 course.instructor_str  | X |  || X| | Used for sorting/faceting by Instructor name
 reading.author_str  | X |  | |X|  Used for sorting/faceting by author name
 reading.journal_str | X |  | |X| Used for sorting/faceting by journal
