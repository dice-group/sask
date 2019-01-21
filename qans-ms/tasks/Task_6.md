# Task 6 of the second phase of project

#### Creation of SPARQL query templates.

Before getting into the formation of SPARQL query templates, let's first know about POS tagging.

POS tagging is a process in which we mark up a word in the text or corpus as belonging to a specific part of speech, based both on its definition and its context.

Say we have a sentence, _**An apple a day keeps the doctor away**_.

Each word in the sentence corresponds to a particular part of speech as shown below:

**Word** | **POS**
---------|---------
_An_     |   DT
_apple_  |   NN
_a_      |   DT
_day_    |   NN
_keeps_  |   VBZ
_the_    |   DT
_doctor_ |   NN
_away_   |   RB

Here is the list of Penn treebank POS tag set:

1. CC Coordinating conjunction
2. CD Cardinal number
3. DT Determiner
4. EX Existential there
5. FW Foreign word
6. IN Preposition or subordinating conjunction
7. JJ Adjective
8. JJR Adjective, comparative
9. JJS Adjective, superlative
10. LS List item marker
11. MD Modal
12. NN Noun, singular or mass
13. NNS Noun, plural
14. NNP Proper noun, singular
15. NNPS Proper noun, plural
16. PDT Predeterminer
17. POS Possessive ending
18. PRP Personal pronoun
19. PRP$ Possessive pronoun
20. RB Adverb
21. RBR Adverb, comparative
22. RBS Adverb, superlative
23. RP Particle
24. SYM Symbol
25. TO to
26. UH Interjection
27. VB Verb, base form
28. VBD Verb, past tense
29. VBG Verb, gerund or present participle
30. VBN Verb, past participle
31. VBP Verb, non-3rd person singular present
32. VBZ Verb, 3rd person singular present
33. WDT Wh-determiner
34. WP Wh-pronoun
35. WP$ Possessive wh-pronoun
36. WRB Wh-adverb

If you want your sentence to be tagged with parts of speech you can use Stanford CoreNLP's POS tagger (software that reads text in some language and assigns parts of speech to each word) as provided in the [link](http://corenlp.run/).


On how to create SPARQL templates and everything regarding it in bit detail is contained in the PDF provided in the [link](https://github.com/dice-group/SurniaQA/blob/master/docs/KG_Miniproject_Presentation.pdf).




