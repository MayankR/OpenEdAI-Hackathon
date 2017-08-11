## API Reference

### Endpoints
- **[<code>GET</code> /search/keyword](#search-for-keyword)**
- **[<code>GET</code> /convertpdf/html](#convert-pdf-to-html)**
- **[<code>GET</code> /getQuestion/](#get-questions-from-text)**
- **[<code>GET</code> /getRelatedConcepts/](#get-related-concepts-from-the-text)**
- **[<code>GET</code> /getRelatedConcepts/ocr](https://github.com/MayankR/OpenEdAI-Hackathon/blob/master/Server-side/README.md)**
- **[<code>GET</code> /getText/ocr](https://github.com/MayankR/OpenEdAI-Hackathon/blob/master/Server-side/README.md)**

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
    **Content:** `{"api":"search api","message":"successful","text":"<div style=\"text-align: justify !important;\"><p>A  <strong>man</strong> is a male human. The term <em>man</em> is usually reserved for an adult male, with the term boy being the usual term for a male child or adolescent. However, the term <em>man</em> is also sometimes used to identify a male human, regardless of age, as in phrases such as \"men's basketball\".</p>\n<p>Like most other male mammals, a man's genome typically inherits an X&nbsp;chromosome from his mother and  a Y&nbsp;chromosome from his father. The male fetus produces larger amounts of androgens and smaller amounts of estrogens than a female fetus. /....."}s`
    
    
    
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
    **Content:** `{"api":"get Related Concepts","message":"successful","text":{"usage":{"text_units":1,"text_characters":717,"features":2},"language":"en","keywords":[{"text":"Mayweather vs Conor","relevance":0.93821},{"text":"great boxer Mayweather","relevance":0.867059},{"text":"professional boxing fight","relevance":0.809104},{"text":"cross-code money-spinning abomination","relevance":0.777159},{"text":"biggest star McGregor","relevance":0.772441},{"text":"boxing match","relevance":0.575037},{"text":"Las Vegas","relevance":0.504324},{"text":"animosity","relevance":0.391795},{"text":"certainty","relevance":0.38205},{"text":"UFC","relevance":0.372879},{"text":"rivalry","relevance":0.364877},{"text":"speculation","relevance":0.357864},{"text":"millennium","relevance":0.350905},{"text":"sides","relevance":0.349247},{"text":"point","relevance":0.336993},{"text":"view","relevance":0.336934},{"text":"name-calling","relevance":0.331425},{"text":"record","relevance":0.331258}],"concepts":[{"text":"Boxing","relevance":0.980902,"dbpedia_resource":"http://dbpedia.org/resource/Boxing"},{"text":"Ricky Hatton","relevance":0.900841,"dbpedia_resource":"http://dbpedia.org/resource/Ricky_Hatton"},{"text":"Manny Pacquiao","relevance":0.785102,"dbpedia_resource":"http://dbpedia.org/resource/Manny_Pacquiao"},{"text":"McGregor, Texas","relevance":0.701043,"dbpedia_resource":"http://dbpedia.org/resource/McGregor,_Texas"},{"text":"Julio César Chávez","relevance":0.700916,"dbpedia_resource":"http://dbpedia.org/resource/Julio_César_Chávez"},{"text":"Carmen Basilio","relevance":0.674608,"dbpedia_resource":"http://dbpedia.org/resource/Carmen_Basilio"},{"text":"Pernell Whitaker","relevance":0.671624,"dbpedia_resource":"http://dbpedia.org/resource/Pernell_Whitaker"},{"text":"Savate","relevance":0.658305,"dbpedia_resource":"http://dbpedia.org/resource/Savate"}]}}`
    
    
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
    **Content:** `{"api":"get Related Concepts from image","message":"successful","text":{"usage":{"text_units":1,"text_characters":882,"features":2},"language":"en","keywords":[{"text":"Xlv favorite maitre","relevance":0.947183},{"text":"fortu-   COnfirmed reservation","relevance":0.82052},{"text":"overbooked hotels","relevance":0.697043},{"text":"overbooked restaurant","relevance":0.687725},{"text":"Goldie Hawn","relevance":0.673972},{"text":"Steffi Graf","relevance":0.670627},{"text":"reservation book","relevance":0.647088},{"text":"last-minute approach.","relevance":0.64552},{"text":"busy night","relevance":0.641915},{"text":"eaav people","relevance":0.635971},{"text":"fake celebrity","relevance":0.624951},{"text":"20Sh restaurant","relevance":0.615549},{"text":"loudmouthed man","relevance":0.613838},{"text":"popular hotel","relevance":0.598313},{"text":"adage","relevance":0.482061},{"text":"love","relevance":0.472158},{"text":"ingenuity","relevance":0.47098},{"text":"hi","relevance":0.453926},{"text":"hell","relevance":0.444239},{"text":"Rob","relevance":0.443884},{"text":"Price","relevance":0.442767},{"text":"Anythinq","relevance":0.442664},{"text":"war","relevance":0.441891},{"text":"phonine","relevance":0.438185},{"text":"party","relevance":0.438018},{"text":"a-rives","relevance":0.43797},{"text":"*Look","relevance":0.437298}],"concepts":[{"text":"Steffi Graf","relevance":0.932431,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Steffi_Graf"},{"text":"Goldie Hawn","relevance":0.915043,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Goldie_Hawn"},{"text":"Golden Globe Award for Best Actress – Motion Picture Musical or Comedy","relevance":0.642367,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Golden_Globe_Award_for_Best_Actress_–_Motion_Picture_Musical_or_Comedy"},{"text":"Hotel","relevance":0.641934,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Hotel"},{"text":"Academy Award for Best Actress","relevance":0.629773,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Academy_Award_for_Best_Actress"},{"text":"Maître d'","relevance":0.627417,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Maître_d'"},{"text":"Love","relevance":0.615825,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Love"},{"text":"Graf","relevance":0.614835,"dbpedia_resource":"http:\/\/dbpedia.org\/resource\/Graf"}]}}`
  
