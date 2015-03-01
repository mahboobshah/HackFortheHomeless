from flask import Flask, request, jsonify, Response
import MySQLdb
import datetime
import simplejson as json

paypalhack = Flask(__name__)

@paypalhack.route('/')

def index():
    return jsonify(status=True)

@paypalhack.route('/registration', methods=['POST'])

def insertseeker():
   db = MySQLdb.connect("localhost","root","feaP0hSvMYSQL","hackathon")
   cursor = db.cursor()
   # Get the parsed contents of the form data
   jsont = request.json
   ins_name = jsont["name"]
   ins_gender = jsont["gender"]
   ins_age = int(jsont["age"])
   ins_skillset = jsont["skillset"]


   sqlins = "INSERT INTO seeker (name,gender,age,skillset) \
             VALUES ('%s','%c',%d,'%s')" % (ins_name, ins_gender, ins_age, ins_skillset)
   try:
     cursor.execute(sqlins)
     ins_id = cursor.lastrowid
     db.commit()

     return jsonify(status=True,id=ins_id)

   except Exception, err:
      # Rollback in case there is any error
      print(cursor._last_executed)
      print (Exception, err)
      return jsonify(status=False)


@paypalhack.route('/rating', methods=['POST'])

def insertrating():
   db = MySQLdb.connect("localhost","root","feaP0hSvMYSQL","hackathon")
   cursor = db.cursor()
   # Get the parsed contents of the form data
   jsont = request.json
   ins_id = int(jsont["id"])
   ins_rating = int(jsont["rating"])
   ins_review = jsont["review"]

   sqlins = "INSERT INTO ratings (id,rating,review) \
             VALUES (%d,%d,'%s')" % (ins_id, ins_rating, ins_review)
   try:
     cursor.execute(sqlins)
     db.commit()

     return jsonify(status=True)

   except Exception, err:
      # Rollback in case there is any error
      print(cursor._last_executed)
      print (Exception, err)
      return jsonify(status=False)

@paypalhack.route('/profile', methods=['POST'])

def getprofile():
   db = MySQLdb.connect("localhost","root","feaP0hSvMYSQL","hackathon")
   cursor = db.cursor(MySQLdb.cursors.DictCursor)
   # Get the parsed contents of the form data
   jsont = request.json
   sel_id = int(jsont["id"])

   sqlavg = "select avg(rating) as andaza, count(rating) as cnt from ratings where id = %d " % (sel_id)

   try:
     cursor.execute(sqlavg)
     sel_avg = cursor.fetchone()
     rating_review = "\nAverage Rating: "+ str(sel_avg["andaza"])
     total = "\nTotal works done: "+ str(sel_avg["cnt"]) + "\nLatest Reviews:\n"
     try:
        cursor.execute(sqlsel)
        sel_revw = cursor.fetchall()
        reviews = ""

        for row in sel_revw:
            reviews += "Rating: "+ str(row["rating"]) + "\tReview: " + str(row["review"]) + "\n"
        try:
           cursor.execute(sqlprof)
           sel = cursor.fetchone()

           profile = "\nName: "+sel["name"]+"\nAge: "+str(sel["age"])+"\nSkillset: " +sel["skillset"]
           return_string = "Profile:\n" + profile+ rating_review + total+ reviews
           return return_string
        except Exception, err:
           # Rollback in case there is any error
           print(cursor._last_executed)
           print (Exception, err)
           return jsonify(status=False)

     except Exception, err:
        # Rollback in case there is any error
        print(cursor._last_executed)
        print (Exception, err)
        return jsonify(status=False)

   except Exception, err:
      # Rollback in case there is any error
      print(cursor._last_executed)
      print (Exception, err)
      return jsonify(status=False)

if __name__ == '__main__':

    paypalhack.run(port=80, debug=True)

