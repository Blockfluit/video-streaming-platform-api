

# Video streaming platform REST API

#### <code>/api/v1</code> <code>/</code> <code>auth</code> 

<details>
    <summary><code>POST</code> <code>/register</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |token|optional|text|Invite token|

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |email|optional|text|-|
> |username|required|text|-|
> |password|required|text|-|
</details>
<details> 
    <summary><code>POST</code> <code>/authenticate</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |username|required|text|-|
> |password|required|text|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>media</code>

<details>
    <summary><code>GET</code> <code>/</code> <code>{id}</code> </summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|optional|integer|id of media|
</details>
<details>
    <summary><code>POST</code></summary>

##### Form-data Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |name|required|text|-|
> |thumbnail|required|file|jpg, jpg or png|
> |trailer|optional|text|link to trailer|
> |type|required|text|"MOVIE", "SERIES" or "ANIME"|
> |genres|required|text|comma separated|
> |actors|optional|text|comma separated. ids of actors not there name|
</details>
<details>
    <summary><code>PATCH</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
> |name|optional|text|-|
> |thumbnail|optional|binary|-|
> |trailer|optional|text|link to trailer|
> |type|optional|text|"MOVIE", "SERIES" or "ANIME"|
> |genres|optional|text|-|
> |actors|optional|text|-|
</details>
<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>users</code>

<details>
    <summary><code>GET</code> </summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>
<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |username|required|text|-|
> |password|required|text|-|
> |email|optional|text|-|
> |role|required|text|"user", "critic" or "admin"|
</details>
<details>
    <summary><code>PATCH</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |username|required|text|-|
> |email|optional|text|-|
> |role|optional|text|"user", "critic" or "admin"|
</details>
<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |username|required|text|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>change-password</code>

<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |email|required|text|-|
</details>

<details>
    <summary><code>PATCH</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |token|required|text|-|

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |password|required|text|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>actors</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>
<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |firstname|required|text|-|
> |lastname|required|text|-|
</details>

<details>
    <summary><code>PATCH</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
> |firstname|optional|text|-|
> |lastname|optional|text|-|
</details>
<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>genres</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>
<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |genre|required|text|-|
</details>

<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |genre|required|text|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>tickets</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>
<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |title|required|text|-|
> |comment|required|text|-|
> |type|required|text|"issue", "suggestion" or "other"|
</details>
<details>
    <summary><code>PATCH</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
> |type|required|text|"issue", "suggestion" or "other"|
</details>

<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>requests</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>
<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |name|required|text|-|
> |year|required|integer|-|
> |comment|optional|text|-|
</details>

<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>invite-tokens</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>


<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |expiration|required|datetime|example: 2024-02-09T11:19:42.12Z|
> |role|required|text|"user", "critic", "admin"|
</details>
<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |token|required|text|-|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>watchlist</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>


<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of media|
</details>
<details>
    <summary><code>DELETE</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of media|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>stream</code>

<details>
    <summary><code>GET</code> <code>/video</code> <code>/</code> <code>{id}</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of the video|
</details>
<details>
    <summary><code>GET</code> <code>/subtitle</code> <code>/</code> <code>{id}</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of the subtitle|
</details>
<details>
    <summary><code>GET</code> <code>/thumbnail</code> <code>/</code> <code>{id}</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of the corresponding media|
</details>

---
#### <code>/api/v1</code> <code>/</code> <code>watched</code>

<details>
    <summary><code>GET</code></summary>

##### URL Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |-|-|-|-|
</details>


<details>
    <summary><code>POST</code></summary>

##### Body Parameters
> |name|type|data type|description|
> |-|-|-|-|
> |id|required|integer|id of video|
> |timestamp|required|float|current time of video|
</details>