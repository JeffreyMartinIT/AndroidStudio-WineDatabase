# Beverage App

## Author

Jeffrey Martin

## Description

Application to view the contents of a beverage list.
The master part will display a list of all of the beverage items in the list.
The Detail part will display the details of a specific beverage item.
The User should be able to go up and down items in the list by swiping left or right on a detail page (View Pager).

Once a user is on the detail page for a beverage item, there are additional buttons added that can be pressed. A Select Contact Button, and a Send Email button. The Select Contact button will allow a user to select a contact from the contacts application. The Send Email button will allow the user to send an email to the contact that was selected.

If the device has a default Contacts App, then the Select Contact button will be enabled, and the Send Email button disabled until a contact is selected. If the application does not have a default Contacts App, the application will disable the Select Contacts button and enable the Send Email button. If it is the case where a contacts app does not exist, the user can still send an email, however there will be no way to pre-populate the 'to' field nor personalize it.

When clicking the Select Contact button, the app launches the default contacts app and allow the user to select a contact to send a email to.
When clicking the Send Email button, the app should take the email address of the selected user, and use that as the recipient for the email. The email should have a Subject auto-generated based on a pre-defined message. The body of the email should address the contact that was selected by using the contacts name. This will help personalize the message. Lastly, the email body should also contain all of the information about the specific item.

The exact same data can be pulled by making a HTTP request to the following URL. If you would like to see what gets returned, you can can simply type the following URL into a web brower and see what gets returned.

http://barnesbrothers.homeserver.com/beverageapi

## Outside Resources Used
stackoverflow.com
tutorialspoint.com
developer.android.com

## Known Problems, Issues, And/Or Errors in the Program
When email is sent to outlook, outlook will strip out line returns, from the body text, if the settings have not been changed by user.  All other communication apps work on default settings.




