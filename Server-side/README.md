## API Reference

### Endpoints
- **[<code>GET</code> /search/keyword](#search-for-keyword)**
- **[<code>GET</code> /convertpdf/html](#convert-pdf-to-html)**
- **[<code>GET</code> /getQuestion/](#get-questions-from-text)**
- **[<code>GET</code> /getRelatedConcepts/](#get-related-concepts-from-the-text)**
- **[<code>GET</code> /getRelatedConcepts/ocr](#get-related-concepts-from-the-image-after-ocr)**
- **[<code>GET</code> /getText/ocr](#get-text-from-the-image-after-ocr)**

## Search for Keyword ##
----
  _Retrieves Text from Wikipedia for the given keyword_

* **URL**

  _/search/keyword_

* **Method:**
  
  _GET_
  
*  **URL Params**

   **Required:**
 
   `query=[string]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"search api","message":"successful","text":"<div style=\"text-align: justify !important;\"><p>A  <strong>man</strong> is a male human. The term <em>man</em> is usually reserved for an adult male, with the term boy being the usual term for a male child or adolescent. However, the term <em>man</em> is also sometimes used to identify a male human, regardless of age, as in phrases such as \"men's basketball\".</p>\n<p>Like most other male mammals, a man's genome typically inherits an X&nbsp;chromosome from his mother and  a Y&nbsp;chromosome from his father. The male fetus produces larger amounts of androgens and smaller amounts of estrogens than a female fetus. /....."}s`
    
## Convert PDF to HTML 
----
  _Retrieves Text from Wikipedia for the given keyword_

* **URL**

  _/convertpdf/html_

* **Method:**
  
  _POST_
  
*  **DATA Params**

   **Required:**
 
   `name=[string]`  -- File Extension
   `file=[base64]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"Convert PDF to HTML","message":"successful","text":"<?xml version='1.0' encoding='UTF-8' standalone='yes'?><html>\n<head>\n    <meta content=\"text\/html; charset=UTF-8\" http-equiv=\"Content-Type\"\/><meta content=\"iit delhi\" name=\"author\"\/><meta content=\"2017-04-26\" name=\"publicationdate\"\/><title>no title<\/title><\/head>\n<body><p> <b>INDIAN INSTITUTE OF TECHNOLOGY DELHI <\/b><\/p><p>ACADEMIC &amp; EXAMINATION SECTION (PGS&amp;R) <b> <\/b><\/p><p>IITD\/A&amp;E(PGS)\/C\/2017\/40414 Dated: 26.04.2017 <\/p><p><b>Fee Circular for PG students (1<\/b><b>st <\/b><b>Semester 2017-2018) <\/b><\/p><p>All Postgraduate students are requested to make payment of fees for 1st <b>Semester 2017-2018"}`
    
    
    
 ## Get Questions from Text
----
  _Retrieves Questions from Text Provided_

* **URL**

  _/getQuestion/_

* **Method:**
  
  _GET_
  
*  **URL Params**

   **Required:**
 
   `text=[string]`  

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"get Question","message":"successful","text":[{"answer":"speculation","question":"After all the __________ about whether we would have the fight, the last few weeks have seen much name-calling and animosity on both sides, as the rivalry intensifies ahead of the big day.","similar_words":["adverse opinion","guess","side"],"title":"mytopic"}]}`
    
 **Get Related Concepts from the Text**
----
  _Retrieves Concepts from Text Provided_

* **URL**

  _/getRelatedConcepts/_

* **Method:**
  
  _GET_
  
*  **URL Params**

   **Required:**
 
   `text=[string]`  

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"get Related Concepts","message":"successful","text":{"usage":{"text_units":1,"text_characters":717,"features":2},"language":"en","keywords":[{"text":"Mayweather vs Conor","relevance":0.93821},{"text":"great boxer Mayweather","relevance":0.867059},{"text":"professional boxing fight","relevance":0.809104},{"text":"cross-code money-spinning abomination","relevance":0.777159},...],"concepts":[{"text":"Boxing","relevance":0.980902,"dbpedia_resource":"http://dbpedia.org/resource/Boxing"},{"text":"Ricky Hatton","relevance":0.900841,"dbpedia_resource":"http://dbpedia.org/resource/Ricky_Hatton"},...]}}`
    
    
**Get Related Concepts from the Image after OCR**
----
  _Retrieves Concepts from Text in Image Provided_

* **URL**

  _/getRelatedConcepts/ocr_

* **Method:**
  
  _POST_
  
*  **DATA Params**

   **Required:**
 
    `name=[string]`  -- File Extension
   `file=[base64]` 

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"get Related Concepts from image","message":"successful","text":{"usage":{"text_units":1,"text_characters":882,"features":2},"language":"en","keywords":[{"text":"Xlv favorite maitre","relevance":0.947183},{"text":"fortu-   COnfirmed reservation","relevance":0.82052},{"text":"overbooked hotels","relevance":0.697043},{"text":"overbooked restaurant","relevance":0.687725}....],"concepts":[{"text":"Steffi Graf","relevance":0.932431,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Steffi_Graf"},{"text":"Goldie Hawn","relevance":0.915043,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Goldie_Hawn"},....]}}`
    
    
**Get Text from the Image after OCR**
----
  _Retrieves Text in Image Provided_

* **URL**

  _/getText/ocr_

* **Method:**
  
  _POST_
  
*  **DATA Params**

   **Required:**
 
    `name=[string]`  -- File Extension
   `file=[base64]` 

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{"api":"get Text from image","message":"successful","text":"I am the sample text on the image"}`
  
  
