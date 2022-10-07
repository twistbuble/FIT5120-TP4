#!/usr/bin/env python
# coding: utf-8

# ### Libraries Required

# In[1]:


import pandas as pd
from sqlalchemy import create_engine
import mysql.connector
import pymysql
from pyspark import SparkContext
from pyspark.sql.session import SparkSession
from pyspark.sql.types import IntegerType, StringType
from pyspark.ml.evaluation import RegressionEvaluator
from pyspark.ml.recommendation import ALS


# ### Initiate Spark Session and Database Connection

# In[2]:


appName = "Meal Recipe Collaborative Filtering"
master = "local[*]"

spark = SparkSession.builder.master(master).appName(appName).getOrCreate()

sc = spark.sparkContext
sc.setLogLevel("WARN")

# Establish a connection
sqlEngine = create_engine('mysql+pymysql://root:5177FC9E-8C6B@54.88.63.24:3306/food_intol')


# ### Obtaining user ratings from Database

# In[16]:


# Connecting to database
dbConnection    = sqlEngine.connect()

# Storing user ratings to pandas dataframe
panda_df = pd.read_sql('SELECT * FROM User_Ratings', dbConnection)

# Ending
dbConnection.close()


# ### Formatting and transform Panda Dataframe to Spark Dataframe

# In[17]:


# turn pandas df into spark df for training
test_user_df = spark.createDataFrame(panda_df)

# convert user_id to string
test_user_df = test_user_df \
    .withColumn('user_id', test_user_df['user_id'].cast(IntegerType()))


# In[18]:


test_user_df.show()


# ### Model Training

# In[7]:


# train / test split
train, test = test_user_df.randomSplit([0.8, 0.2])

# define ALS model hyperparameters
als = ALS(maxIter=4, regParam=0.1, userCol="user_id", itemCol="recipe_index", ratingCol="rating",
          coldStartStrategy="drop")
model = als.fit(train)


# #### Model Performance

# In[8]:


# apply model to test
# predictions = model.transform(test)

# eval = RegressionEvaluator(metricName="rmse", labelCol="Rating", predictionCol="prediction")
# rmse = eval.evaluate(predictions)
# print("Root-mean-square error = " + str(rmse))


# MSE is quite poor, suggesting that more data is required to provide more reliable predictions<br>
# Ideally we should have below 1.0 RMSE

# ### Extracting Features (Pandas)

# In[9]:


userRecs = model.recommendForAllUsers(50)


# In[10]:


pd.set_option('display.max_colwidth', None)
user_predictions = userRecs.toPandas()

user_predictions['user_id'] = user_predictions['user_id'].astype(str)


# In[11]:


user_predictions.head(10)


# ### Extract Recommendations
# 
# Run this function to get the N-number of recommendations that should be shown to the user
# 
# extractRecommendations(ratings_df, predictions_df, user_id, num_of_recommendations)<br>
# 
# Where:<br><br>
# <b>ratings_df</b><br> refers to the user data table from the database, including user_id, recipe_id, recipe_ratings<br>
# 
# <b>predictions_df</b><br> is the recomemndations for each user based on the trained model<br>
# 
# <b>user_id</b><br> being the unique user identifying number<br>
# 
# <b>num_of_recommendations</b><br> being the number of recommendations you wish to output<br>

# In[12]:


def extractRecommendations(ratings_df, predictions_df, user_id, num_of_recommendations):
    
    predicted_recipes = []
    user_ratings = ratings_df[ratings_df['user_id'] == user_id]['recipe_index'].tolist()
    
    for item in predictions_df[predictions_df['user_id'] == user_id]['recommendations'].tolist()[0]:
        predicted_recipes.append(item[0])
        
    return [x for x in predicted_recipes if x not in user_ratings][:num_of_recommendations]
    


# In[13]:


user_list = []
for index, row in user_predictions.iterrows():
    user_list.append(row[0])

recommended_recipes = []
for user in user_list:
    recommended_recipes.append(extractRecommendations(panda_df, user_predictions, user, 10))
    
output_df = pd.DataFrame({'User_ID': user_list, 'Recommendations': recommended_recipes})
output_df['Recommendations'] = output_df['Recommendations'].apply(lambda x: ', '.join(map(str, x)))


# ### Pushing new recommendations to Database

# In[15]:


dbConnection = sqlEngine.connect()

print("Database connection established")

frame = output_df.to_sql('User_Recommendations', dbConnection, if_exists='replace', index=False)

print("Database has been updated successfully")

dbConnection.close()

print("Database connection closed")

