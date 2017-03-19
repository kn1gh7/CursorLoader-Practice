# CursorLoader-Practice

Displays a list of all the music files in the device by different categories like Author, Genre, etc. It also fetches all the songs from all the categories and lists them. 

All the tabs use Cursor Loader and has unique IDs for each Tab. It uses the Android MediaStore API for each Category and fetches the list using CursorLoader, which fetches the data in a background thread and returns the Cursor and then data is obtained from the Cursor and adapter is updated with the list.
