import pickle
import tensorflow as tf
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.layers import Embedding
from keras.layers import Dense, Input, Flatten
from keras.layers import Conv2D, MaxPool2D, Embedding, Merge, Dropout, Reshape, Concatenate
from keras import regularizers
from keras.models import Model
from keras.models import model_from_json
from keras import optimizers
from keras.callbacks import TensorBoard
from keras import losses
from keras.utils import to_categorical
import time
import numpy as np
import pandas as pd
from scipy import stats
import os

with open('X_data.pickle', 'rb') as handle:
    X_data = pickle.load(handle)

with open('Y_data.pickle', 'rb') as handle:
    Y_data = pickle.load(handle)

print(len(X_data),len(Y_data))
#
# for i,j in zip(X_data,Y_data):
#     print(i[0:5],j)

tokenizer = Tokenizer()
tokenizer.fit_on_texts(X_data)
documents = tokenizer.texts_to_sequences(X_data) # documents arranged in sequence of words each word in indexed by word_index
NO_WORDS = len(tokenizer.word_index) + 1


Y_data = to_categorical(np.asarray(Y_data))


# In[6]:


max_doc_length = 1500
data = pad_sequences(documents, maxlen=max_doc_length,padding='post') # this will make a matrix of documents of 1000 dimensional vec


X_train = data[0:2000]
X_val = data[2000:2200]
X_test = data[2200:]
Y_train = Y_data[0:2000]
Y_val = Y_data[2000:2200]
Y_test = Y_data[2200:]
# In[10]:
# print(embedding_mat)


# In[7]:


embeddings_index = dict()
embeddings_dim = 100
with open('googlenews.txt','r',encoding="utf8") as f:
    for line in f:
        values = line.split()
        word = values[0]
        coefs = np.asarray(values[1:101], dtype='float32')
        embeddings_index[word] = coefs


# In[8]:


embedding_mat = np.random.random([NO_WORDS,embeddings_dim]) # word2vec implementation dim = no of words * 100
for word, i in tokenizer.word_index.items():
    embedding_vector = embeddings_index.get(word)
    if embedding_vector is not None:
        embedding_mat[i] = embedding_vector


# In[9]:


#
inputs = Input(shape=(max_doc_length,), dtype='int32')


# In[11]:


embedding = Embedding(input_dim=len(tokenizer.word_index) + 1, output_dim=embeddings_dim ,weights=[embedding_mat], input_length=max_doc_length,trainable=False)(inputs)
reshape = Reshape((max_doc_length,embeddings_dim,1))(embedding)


# In[12]:


filter_sizes = [3,4,5]
num_filters = 100
drop = 0.5
conv_0 = Conv2D(num_filters, kernel_size=(filter_sizes[0], embeddings_dim), padding='valid', kernel_initializer='normal', activation='relu')(reshape)
conv_1 = Conv2D(num_filters, kernel_size=(filter_sizes[1], embeddings_dim), padding='valid', kernel_initializer='normal', activation='relu')(reshape)
conv_2 = Conv2D(num_filters, kernel_size=(filter_sizes[2], embeddings_dim), padding='valid', kernel_initializer='normal', activation='relu')(reshape)


# In[17]:


epochs = 20
batch_size = 50


# In[18]:


maxpool_0 = MaxPool2D(pool_size=(max_doc_length - filter_sizes[0] + 1, 1), strides=(1,1), padding='valid')(conv_0)
maxpool_1 = MaxPool2D(pool_size=(max_doc_length - filter_sizes[1] + 1, 1), strides=(1,1), padding='valid')(conv_1)
maxpool_2 = MaxPool2D(pool_size=(max_doc_length - filter_sizes[2] + 1, 1), strides=(1,1), padding='valid')(conv_2)


# In[19]:


concatenated_tensor = Concatenate(axis=1)([maxpool_0, maxpool_1, maxpool_2])
flatten = Flatten()(concatenated_tensor)
dropout = Dropout(drop)(flatten)
# output1 = Dense(units=100, activation='relu')(dropout)
# dropout1 = Dropout(drop)(output1)
output = Dense(units=5,activation='softmax')(dropout)


# In[68]:


model = Model(inputs=inputs, outputs=output)

dir_path = os.path.dirname(os.path.realpath(__file__))
tensorboard = TensorBoard(log_dir=dir_path+"/logs/{}".format(time.time()))
adam = optimizers.Adam(lr=1e-3, beta_1=0.9, beta_2=0.999, epsilon=1e-08, decay=0.0)
model.compile(optimizer=adam, loss='categorical_crossentropy', metrics=['categorical_accuracy','recall','precision','mcor'])
print("Training Model...")
model.summary()
model.fit(X_train, Y_train, batch_size=batch_size, epochs=epochs, verbose=1, validation_data=(X_val, Y_val),callbacks=[tensorboard])  # starts training
print(model.evaluate(X_test,Y_test))


with open('tokenizer.pickle', 'wb') as handle:
    pickle.dump(tokenizer, handle, protocol=pickle.HIGHEST_PROTOCOL)

model_json=model.to_json()
with open('model.json','w') as f:
    f.write(model_json)

model.save_weights('model.h5')
print('model is saved')
