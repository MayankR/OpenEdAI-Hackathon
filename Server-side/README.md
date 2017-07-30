## API Reference

+request(POST, application/json){

    { 'topic1': 'text-something',
      'topic2': 'text2'
    }

}
+response(application/json){

[
    {
        "answer": "hero",
        "question": "You die a __________ or stay long enough to become a vilan",
        "similar_words": [
            "aristocrat",
            "boss",
            "caller",
            "captain",
            "cheerleader",
            "civic leader"
        ],
        "title": "topic1"
        }
    ]

}
