# CSGLScraper
A small Java program to filter trades on CS:GO Lounge

To get started go to CS:GO Lounge, and search as you'd do it, and paste the results page's URL in to the first text field to
get the trades. 
If you want to just filter only the first page of the results then paste the URL of the first page.
But if you want to filter all of the pages then paste the URL of the 2nd page (although every page other than the first 
should do the work)
Keep in mind that getting the trades from CS:GO Lounge may take some seconds.

To set filter write your "filter words" into the 2nd text field, separated by spaces. At the moment a filter word can only be
one word, as a 2-word filter word would be treated as 2 separate filter words.
In the left column the text around the first match of a filter word in the description will be shown, but only if the button is set to "Contains". 

The Contains button sets whether the results should or should not contain the filter words indicated by what text is on the button. 
Example: If filter word is "quicksell" and button is not pressed, then the program will only show trades which contain 
"quicksell". Otherwise it shows trades which do not contain "quicksell".
If the No. of keys button is set to 0, and the filter text field is empty then, all of the trades will be shown.

The No. of Keys button is an alternative filter, which ignores the filter text field, and sets the filters to "2 k" and "2k" if the value of the button is 2. 

Clicking on the links in the right column will open a new page with the link in your default browser.


