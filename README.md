# Document Summarization and Classification using Deep Learning

This is a JavaFX application developed on IntelliJ. The summarization module is part of the Java application, while the classification module is developed in Python using TensorFlow. The `predict_v-2.0.py` file in the `/SnC/Models/BBC/` folder is the classification script used to determine the class based on the document summary.

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

## Machine Learning Solution

We have proposed an innovative solution for context-aware document classification by combining Recurrent Neural Networks (RNN) with Convolutional Neural Networks (CNN). This hybrid approach, often referred to as an RCNN, leverages the strengths of both architectures:

- The CNN component excels at capturing local features and spatial relationships within the text.
- The RNN component, typically using LSTM or GRU units, captures sequential dependencies and long-range context.

By combining these two powerful neural network architectures, our model can better understand the contextual nuances of documents, leading to more accurate and robust classification results. This approach is particularly effective for documents with complex structures or those requiring a deep understanding of context for proper categorization.

## Java Desktop Application

Our Java desktop application includes a powerful summarization feature that utilizes the Google PageRank algorithm. This feature provides concise and informative summaries of input documents, which are then used for classification. Key aspects of the application include:

- **Summarization**: Implements the Google PageRank algorithm to identify the most important sentences in a document, creating a coherent and representative summary.
- **Classification**: Uses the generated summaries to classify documents into predefined categories.
- **Automatic Sorting**: Organizes all input documents into different category directories based on their classification results.
- **User-Friendly Interface**: Built with JavaFX to provide an intuitive and responsive user experience.

This desktop application streamlines the process of document management by automating the tasks of summarization, classification, and organization, making it an invaluable tool for handling large volumes of documents efficiently.

## Installation and Setup

Note: Place the SnC folder from the project root in the C drive of your computer. The project has an executable build which will run only on systems with JRE 1.8.0_161.
