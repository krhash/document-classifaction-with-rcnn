import os
import numpy as np
import pickle
from random import shuffle
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer

stop_words = set(stopwords.words('english'))
tokenizer = RegexpTokenizer(r'[a-z]+')

parent_dir = "bbc"
dirs = os.listdir(parent_dir)

cat_doc_files = []
cat_labels = []
cat1 = np.array([1,0,0,0,0])
cat2 = np.array([0,1,0,0,0])
cat3 = np.array([0,0,1,0,0])
cat4 = np.array([0,0,0,1,0])
cat5 = np.array([0,0,0,0,1])

cats = []
cats.append(cat1)
cats.append(cat2)
cats.append(cat3)
cats.append(cat4)
cats.append(cat5)

del cats
cats = [0,1,2,3,4]

for path in dirs:
     files = os.listdir(parent_dir + "/" + path)
     doc_files = []
     for fd in files:
         doc = ""
         with open(parent_dir + "/" + path+ "/" +fd,'r') as f:
             doc = f.read()
             tokens = tokenizer.tokenize(doc.lower())
             doc = " ".join(list(filter(lambda x: x not in stop_words,tokens)))
         doc_files.append(doc)
     cat_doc_files.append(doc_files)

print(len(cat_doc_files))

documents_l = []
Y_train = []
for i in range(0,len(cat_doc_files)):
    for j in range(0,len(cat_doc_files[i])):
        documents_l.append(cat_doc_files[i][j])
        Y_train.append(cats[i])


d = []
for i,j in zip(documents_l,Y_train):
    d.append((i,j))

shuffle(d)
shuffle(d)
shuffle(d)
shuffle(d)
shuffle(d)
shuffle(d)
shuffle(d)

print(len(d))

documents_l = []
Y_train = []

for i in d:
    documents_l.append(i[0])
    Y_train.append(i[1])

print(len(documents_l),len(Y_train))


with open('X_train.pickle', 'wb') as handle:
    pickle.dump(documents_l, handle, protocol=pickle.HIGHEST_PROTOCOL)

with open('Y_train.pickle', 'wb') as handle:
    pickle.dump(Y_train, handle, protocol=pickle.HIGHEST_PROTOCOL)
