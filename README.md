# Document Summarization and Classification using Deep Learning

This is a Java desktop application developed on IntelliJ. The summarization module is part of the Java application, while the classification module is developed in Python using TensorFlow. The `predict_v-2.0.py` file in the `/SnC/Models/BBC/` folder is the classification script used to determine the class based on the document summary.

## Features

- Supports PDF document summarization and classification
- Real-time monitoring of a folder to classify newly created documents and move them to class-labeled folders

## Software Requirements

- JRE 1.8.0_161
- Java JRE 1.8.0_161
- Python 3.5.0

## Python Libraries Required

- TensorFlow
- h5py
- SciPy
- NumPy
- Keras

## Classification Module

Our classification module implements an innovative solution for context-aware document classification by combining Recurrent Neural Networks (RNN) with Convolutional Neural Networks (CNN). This hybrid approach, referred to as an RCNN, leverages the strengths of both architectures[1].

### Model Architecture

- CNN component: Captures local features and spatial relationships within the text
- RNN component: Uses LSTM units to capture sequential dependencies and long-range context
- Fully connected layers: For final classification

### Training Phase

1. Data Preprocessing:
   - Tokenization of text
   - Conversion to word embeddings using pre-trained GloVe vectors
   - Padding sequences to uniform length

2. Model Training:
   - Used BBC News dataset with 2225 documents across 5 categories
   - Split data into 80% training and 20% testing sets
   - Trained for 100 epochs with early stopping
   - Used Adam optimizer with categorical cross-entropy loss

### Classification Phase

1. Document Summarization:
   - Utilizes Google PageRank algorithm to generate concise summaries

2. Feature Extraction:
   - Applies trained CNN-RNN model to extract features from summaries

3. Classification:
   - Uses softmax layer for final category prediction

### Model Accuracy

Our model achieved the following performance metrics on the test set:

- Accuracy: 96.8%
- Precision: 97.2%
- Recall: 96.8%
- F1-Score: 97.0%

These results demonstrate the effectiveness of our RCNN approach in accurately classifying documents across different categories[1].

## Java Desktop Application

Our Java desktop application includes a powerful summarization feature that utilizes the Google PageRank algorithm. This feature provides concise and informative summaries of input documents, which are then used for classification. Key aspects of the application include:

- **Summarization**: Implements the Google PageRank algorithm to identify the most important sentences in a document, creating a coherent and representative summary.
- **Classification**: Uses the generated summaries to classify documents into predefined categories.
- **Automatic Sorting**: Organizes all input documents into different category directories based on their classification results.
- **User-Friendly Interface**: Built with JavaFX to provide an intuitive and responsive user experience.

This desktop application streamlines the process of document management by automating the tasks of summarization, classification, and organization, making it an invaluable tool for handling large volumes of documents efficiently.

## Installation and Setup

Note: Place the SnC folder from the project root in the C drive of your computer. The project has an executable build which will run only on systems with JRE 1.8.0_161.
