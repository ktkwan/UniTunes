# import spotipy
# import sys
# from spotipy.oauth2 import SpotifyClientCredentials
#
# spotify = spotipy.Spotify(client_credentials_manager=SpotifyClientCredentials())
# export SPOTIPY_CLIENT_ID ='3db03adb7b634fc7b15eee858b692dfb'
# export SPOTIPY_CLIENT_SECRET ='cfdda93982304a6ba49dad50ba153954'
# export SPOTIPY_REDIRECT_URI='http://example.com'
#
# urn = 'spotify:artist:3jOstUTkEu2JkjvRdBA5Gu'
# sp = spotipy.Spotify()
#
# artist = sp.artist(urn)
# print(artist)
#
# user = sp.user('plamere')
# print(user)

# from spotipy.oauth2 import SpotifyClientCredentials
# import spotipy
# import spotipy.util as util
#
# util.prompt_for_user_token('username',
#                            None,
#                            client_id='3db03adb7b634fc7b15eee858b692dfb',
#                            client_secret='cfdda93982304a6ba49dad50ba153954',
#                            redirect_uri='http://example.com')
# # SPOTIPY_CLIENT_ID = '3db03adb7b634fc7b15eee858b692dfb'
# client_credentials_manager = SpotifyClientCredentials()
# sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
#
# results = sp.search(q='year:2020', limit=20)
# for i, t in enumerate(results['tracks']['items']):
#     print(' ', i, t['name'], t['external_urls']['spotify'])

# from fycharts.SpotifyCharts import SpotifyCharts
# import sqlalchemy

# api = SpotifyCharts()
# connector = sqlalchemy.create_engine("sqlite:///spotifycharts.db", echo=False)
# api.viral50Daily(output_file = "viral_50_daily.csv", output_db = connector, webhook = "https://mywebhookssite.com/post/", start = "2019-01-01", end = "2019-01-31", region = ["us", "global"])

# from fycharts.SpotifyCharts import SpotifyCharts
#
# api = SpotifyCharts()
# api.top200Daily(output_file = "top_200_daily.csv")

from fycharts.SpotifyCharts import SpotifyCharts
import sqlalchemy
import pandas as pd
from spotipy.oauth2 import SpotifyClientCredentials
import spotipy
import spotipy.util as util

import pylast

from csv import writer
from csv import reader




# import sqlite3
#
# sqlite3.connect('songs.db')
# c = conn.cursor()
API_KEY = "929b18632f4e7bfcb29b5fd33c9d6e08"  # this is a sample key
API_SECRET = "04cae11c50a184cb311773ee1e51e1ce"

username = "ktkwan"
password_hash = pylast.md5("lastfm2020!")

network = pylast.LastFMNetwork(api_key=API_KEY, api_secret=API_SECRET,
                               username=username, password_hash=password_hash)


util.prompt_for_user_token('username',
                           None,
                           client_id='3db03adb7b634fc7b15eee858b692dfb',
                           client_secret='cfdda93982304a6ba49dad50ba153954',
                           redirect_uri='http://example.com')

# api = SpotifyCharts()
# connector = sqlalchemy.create_engine("sqlite:///spotifycharts.db", echo=False)
# api.viral50Daily(output_file = "viral_50_daily.csv", output_db = connector, webhook = "https://mywebhookssite.com/post/", start = "2018-08-20", end = "2018-08-20", region = ["us"])
data = pd.read_csv("test2.csv")

client_credentials_manager = SpotifyClientCredentials()
sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

print(dir(sp))
# recommendation_genre_seeds()
track_names = data['Track Name']
artist_names = data['Artist']
ids = data['spotify_id']
no_genres = []
genres_csv = []
duration_ms = []
popularity = []


# genres_csv.append('genre')
for track, artist, id in zip(track_names, artist_names, ids):
    res = sp.search(q='track:' + track, type='track', limit=1)
    # track = sp._get_id()
    track_info = sp.track(id)
    # a = sp.artist()
    # print(track_info['popularity'])
    popularity.append(track_info['popularity'])
    duration_ms.append(track_info['duration_ms'])

    # print(res['tracks']['items'][0])
    print("=========================================")

    results = sp.search(q='artist:' + artist, type='artist', limit=1)
    # print(artist)
    #genre for the artist at this row
    genres = results['artists']['items'][0]['genres']
    # print(genres)
    if (len(genres) == 0):
        no_genres.append(artist)

    genres_csv.append(genres[0])

data['duration'] = duration_ms
data['popularity'] = popularity
keep_col = ['Track Name','Artist','date','region','spotify_id','genres','duration','popularity']
#
new_f = data[keep_col]

new_f.to_csv("songs_with_genre.csv", index=False)

# print(data.columns)




# with open('viral_50_daily.csv', 'r') as read_obj, \
#         open('test1.csv', 'w', newline='') as write_obj:
#     # Create a csv.reader object from the input file object
#     csv_reader = reader(read_obj)
#     # Create a csv.writer object from the output file object
#     csv_writer = writer(write_obj)
#     # Read each row of the input csv file as list
#     for row in csv_reader[1:]:
#
#         # Append the default text in the row / list
#         row.append(genres[0])
#         # Add the updated row / list to the output file
#         csv_writer.writerow(row)

# with open('viral_50_daily.csv', 'r') as read_obj, \
#         open('test1.csv', 'w', newline='') as write_obj:
#     # Create a csv.reader object from the input file object
#     csv_reader = reader(read_obj)
#     # Create a csv.writer object from the output file object
#     csv_writer = writer(write_obj)
#     # Read each row of the input csv file as list
#     for row in csv_reader[1:]:
#         # Append the default text in the row / list
#         row.append(genres[0])
#         # Add the updated row / list to the output file
#         csv_writer.writerow(row)


# print(no_genres)

# default_text = 'Some Text'
# Open the input_file in read mode and output_file in write mode
