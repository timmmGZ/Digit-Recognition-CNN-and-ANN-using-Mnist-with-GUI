# Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI
Training and testing using MNIST dataset (60000train set,1000test set) with 0.5% error (some drawings are out of 28*28, some drawings only half of them are scanned, or wrong labeling and so on)  
There are some MNIST datasets in internet, you could download yourself or download from these links:  
CSV: https://pjreddie.com/projects/mnist-in-csv/  
TXT:https://drive.google.com/drive/folders/10MfF2F5M40NxEFLSpaHWCMo4y8yEMivI  
After download, move them to "/dataset", change the invoking method in Datainfo.java constructor according to your dataset format, in my case, I use the txt one.
```
trainSets = ReadFile.readFromSingleCsv("mnist_train.csv");
trainSets = ReadFile.readFromSingleTxt("mnist_digits_train.txt");
trainSets = ReadFile.readFromSingle???("mnist_digits_train.???");
```

This program is based on Single layer Neural Network with 10 neurons using Softmax as output in both ANN and CNN mode.
# Drawing with CNN Model
![image](https://github.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/blob/master/imagesUpdated/MnistCNNDrawing.gif)
# ANN accuray
Random try--training only 10+ seconds(3-5 epoches), test set 92.61%, train set 93.06% (without using back propagation and keep best weights)
![image](https://raw.githubusercontent.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/master/images/ANN3and5epochAccuracy.png)
# CNN accuray-- train 7 mins to 100%
Training with 16 random designed filters without using back propagation and keep best weights, around 35 epoches is enough to reach 99% acc for train set, but it need more time to reach 100%, I randomly try like below gif, it takes 285 epoches to reach 100% acc, and too
![image](https://github.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/blob/master/imagesUpdated/MnistCNN100Acc.gif)
# The changing of weights 
Following picture describe how weights change when it has only 2,5,10,20,30.....50 data sets respectively to be trained  
Blue=positive, White=0, Red=negative, you could realize how weights work especially obviously in the 4th row and 2nd column example when the dataset only includes number "1" and "4", you could also see the difference between ANN and CNNs' weights in the last row's example 
![image](https://raw.githubusercontent.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/master/images/LessThan50DatasetsWeightsChanges.png)
Here is how weights change between 1st and 5th epoches for ANN
![image](https://raw.githubusercontent.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/master/images/LR0.05Epoch1and5Difference.png)
# False prediction (Overfitting)
I made a function to show random false predictions of a selected number, try to use it after you try the model, and you will find out why the accuracy of a good model base on MNIST dataset should not be more than 99.5% as I mentioned in beginning, unless you accept wrong labeling the numbers(some are even not looking like numbers),
as you notice neuron for number "8" has a large loss all the time, so I will show you some of the false prediction about "8", also check how it influence "9"'s prediction by overfitting
![image](https://raw.githubusercontent.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/master/images/false8.png)
Lets show a bit random numbers' false examples of train set(60000 data sets)
![image](https://raw.githubusercontent.com/timmmGZ/Digit-Recognition-CNN-and-ANN-using-Mnist-with-GUI/master/images/randomFalse.png)
