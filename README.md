# uniTunes

Student’s currently have little exposure to the student audio(music, podcasts, dj set) creators on campus.
Furthermore, student creators don’t have a platform to share their work with other students in a way that 
will garner their work the attention that they deserve.

This is a Brown University specific music application that gives students more exposure to Brown Univesity artists. 

## API's 

I utlize the spotify API in order to get the Brown Artist songs. I use the API in python to create a SQL database of 
Brown specific artists. The Song Database includes the properties: track_name, artist, genre, danceability, tempo, 
acousticness, duration, popularity, etc. 

I would like to incorporate different music platforms in the future, such as Souncloud and Youtube, in order 
to broaden the scope of the platform. 

## Languages 

The backend work is done in both java and python. The frontend work is mainly in html and css utlizing 
freemarker and Spark. 

## How To Run

1. git clone this repository 
2. ./run --gui --database=data/brown_songs.sqlite3 --users=users.sqlite3
3. on a web browser go to "localhost:4566/unitunes" and begin exploring Brown University Music!





