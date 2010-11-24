-------------------------------------------------------------------------------------------

-- QUESTION 1 Which digit yielded the best accuracy of detection by our tests?

-- select number of correctly classified samples (per run and character type)
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct;

-- select total number of samples (per run and character type)
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total;

-- select percentage of correctly classified samples (per run and character type)
select Total.ixRun, Total.sCharacter, Correct.count/Total.count from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter;

+-----------+------------+---------------------------+
| ixRun     | sCharacter | Correct.count/Total.count |
+-----------+------------+---------------------------+
|         1 | 0          |                    0.9745 |
|         1 | 1          |                    0.9730 |
|         1 | 2          |                    0.9737 |
|         1 | 3          |                    0.9734 |
|         1 | 4          |                    0.9700 |
|         1 | 5          |                    0.9721 |
|         1 | 6          |                    0.9729 |
|         1 | 7          |                    0.9745 |
|         1 | 8          |                    0.9717 |
|         1 | 9          |                    0.9717 |
| 201011133 | 0          |                    0.8983 |
| 201011133 | 1          |                    0.8245 |
| 201011133 | 2          |                    0.8226 |
| 201011133 | 3          |                    0.6667 |
| 201011133 | 4          |                    0.7845 |
| 201011133 | 5          |                    0.7513 |
| 201011133 | 6          |                    0.9272 |
| 201011133 | 7          |                    0.8518 |
| 201011133 | 8          |                    0.7874 |
| 201011133 | 9          |                    0.6983 |
| 201011134 | 0          |                    0.9228 |
| 201011134 | 1          |                    0.9041 |
| 201011134 | 2          |                    0.8232 |
| 201011134 | 3          |                    0.6504 |
| 201011134 | 4          |                    0.8001 |
| 201011134 | 5          |                    0.7658 |
| 201011134 | 6          |                    0.9060 |
| 201011134 | 7          |                    0.8585 |
| 201011134 | 8          |                    0.7287 |
| 201011134 | 9          |                    0.7130 |
| 201011141 | 0          |                    0.8856 |
| 201011141 | 1          |                    0.9438 |
| 201011141 | 2          |                    0.8216 |
| 201011141 | 3          |                    0.8156 |
| 201011141 | 4          |                    0.8022 |
| 201011141 | 5          |                    0.7152 |
| 201011141 | 6          |                    0.9002 |
| 201011141 | 7          |                    0.8500 |
| 201011141 | 8          |                    0.7593 |
| 201011141 | 9          |                    0.8455 |
| 201011161 | 0          |                    0.9177 |
| 201011161 | 1          |                    0.9930 |
| 201011161 | 2          |                    0.7416 |
| 201011161 | 3          |                    0.7601 |
| 201011161 | 4          |                    0.7387 |
| 201011161 | 5          |                    0.7244 |
| 201011161 | 6          |                    0.9363 |
| 201011161 | 7          |                    0.8648 |
| 201011161 | 8          |                    0.6481 |
| 201011161 | 9          |                    0.7955 |
| 201011162 | 0          |                    0.9334 |
| 201011162 | 1          |                    0.9214 |
| 201011162 | 2          |                    0.8431 |
| 201011162 | 3          |                    0.7860 |
| 201011162 | 4          |                    0.9237 |
| 201011162 | 5          |                    0.8327 |
| 201011162 | 6          |                    0.9356 |
| 201011162 | 7          |                    0.8374 |
| 201011162 | 8          |                    0.8130 |
| 201011162 | 9          |                    0.8156 |
| 201011231 | 0          |                    0.3213 |
| 201011231 | 1          |                    0.9803 |
| 201011231 | 2          |                    0.2590 |
| 201011231 | 3          |                    0.2754 |
| 201011231 | 4          |                    0.2426 |
| 201011231 | 5          |                    0.2951 |
| 201011231 | 6          |                    0.3475 |
| 201011231 | 7          |                    0.3770 |
| 201011231 | 8          |                    0.2164 |
| 201011231 | 9          |                    0.1902 |
| 201011232 | 0          |                    0.6984 |
| 201011232 | 1          |                    0.9115 |
| 201011232 | 2          |                    0.4557 |
| 201011232 | 3          |                    0.5246 |
| 201011232 | 4          |                    0.5311 |
| 201011232 | 5          |                    0.4525 |
| 201011232 | 6          |                    0.4459 |
| 201011232 | 7          |                    0.5148 |
| 201011232 | 8          |                    0.3049 |
| 201011232 | 9          |                    0.3803 |
+-----------+------------+---------------------------+

-- select character type with largest percentage of correctly classified samples (per run)
select * from (
select Total.ixRun, Total.sCharacter, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter order by ixRun, accuracy desc
) as Accuracies group by Accuracies.ixRun;

+-----------+------------+----------+
| ixRun     | sCharacter | accuracy |
+-----------+------------+----------+
|         1 | 0          |   0.9745 |
| 201011133 | 6          |   0.9272 |
| 201011134 | 0          |   0.9228 |
| 201011141 | 1          |   0.9438 |
| 201011161 | 1          |   0.9930 |
| 201011162 | 6          |   0.9356 |
| 201011231 | 1          |   0.9803 |
| 201011232 | 1          |   0.9115 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 2 Which digit yielded the worst accuracy of detection by our tests?
-- (identical to QUESTION 1 query except order by is asc instead of desc)

select * from (
select Total.ixRun, Total.sCharacter, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter order by ixRun, accuracy asc
) as Accuracies group by Accuracies.ixRun;

+-----------+------------+----------+
| ixRun     | sCharacter | accuracy |
+-----------+------------+----------+
|         1 | 4          |   0.9700 |
| 201011133 | 3          |   0.6667 |
| 201011134 | 3          |   0.6504 |
| 201011141 | 5          |   0.7152 |
| 201011161 | 8          |   0.6481 |
| 201011162 | 3          |   0.7860 |
| 201011231 | 9          |   0.1902 |
| 201011232 | 8          |   0.3049 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 5 What is the accuracy of detection from collected data based on age group?

-- select ages for each sample with age metadata (only consider data from the scanned DataSet)
select Metadata.ixData, Metadata.sValue from Data, Metadata where Data.ixData = Metadata.ixData and Metadata.sKey = 'Age' and Data.ixDataSet = 2;

-- select the number of samples with particular age values
select count(*), convert(sValue,UNSIGNED INTEGER) as age from Data, Metadata where Data.ixData = Metadata.ixData and Metadata.sKey = 'Age' and Data.ixDataSet = 2 group by sValue order by age;

+----------+------+
| count(*) | age  |
+----------+------+
|       10 |    6 |
|      320 |    7 |
|      580 |    8 |
|      730 |    9 |
|      540 |   10 |
|       80 |   11 |
|       10 |   13 |
|      250 |   14 |
|       70 |   15 |
|       90 |   16 |
|      110 |   17 |
|       30 |   18 |
|       10 |   19 |
|       10 |   39 |
|       10 |   45 |
|      120 |   47 |
|       30 |   48 |
|       20 |   49 |
|       10 |   53 |
|       10 |   56 |
|       10 |   57 |
+----------+------+

-- select the number of samples with particular age values (per run)
select Result.ixRun, AgeData.sValue, count(*) from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue;

-- select the number of samples with particular age values which were correctly classified (per run)
select Result.ixRun, AgeData.sValue, count(*) from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue;

-- select percentage of correctly classified samples (per run and age value)
select Total.ixRun, Total.sValue as age, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue) as Total,
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+------+----------+
| ixRun     | age  | accuracy |
+-----------+------+----------+
| 201011231 | 6    |   0.8000 |
| 201011231 | 7    |   0.6813 |
| 201011231 | 9    |   0.4932 |
| 201011231 | 8    |   0.4379 |
| 201011231 | 13   |   0.3000 |
| 201011231 | 10   |   0.2278 |
| 201011231 | 16   |   0.1444 |
| 201011231 | 47   |   0.1333 |
| 201011231 | 18   |   0.1333 |
| 201011231 | 14   |   0.1200 |
| 201011231 | 15   |   0.1143 |
| 201011231 | 11   |   0.1125 |
| 201011231 | 17   |   0.1091 |
| 201011231 | 56   |   0.1000 |
| 201011231 | 49   |   0.1000 |
| 201011231 | 39   |   0.1000 |
| 201011231 | 57   |   0.1000 |
| 201011231 | 53   |   0.1000 |
| 201011231 | 48   |   0.1000 |
| 201011231 | 45   |   0.1000 |
| 201011231 | 19   |   0.1000 |
| 201011232 | 6    |   1.0000 |
| 201011232 | 56   |   1.0000 |
| 201011232 | 7    |   0.8969 |
| 201011232 | 9    |   0.6479 |
| 201011232 | 8    |   0.6103 |
| 201011232 | 13   |   0.6000 |
| 201011232 | 39   |   0.4000 |
| 201011232 | 45   |   0.4000 |
| 201011232 | 10   |   0.3926 |
| 201011232 | 47   |   0.3917 |
| 201011232 | 15   |   0.3429 |
| 201011232 | 11   |   0.3250 |
| 201011232 | 14   |   0.2800 |
| 201011232 | 48   |   0.2667 |
| 201011232 | 18   |   0.2333 |
| 201011232 | 17   |   0.2182 |
| 201011232 | 16   |   0.2111 |
| 201011232 | 57   |   0.2000 |
| 201011232 | 19   |   0.2000 |
| 201011232 | 49   |   0.1000 |
| 201011232 | 53   |   0.1000 |
+-----------+------+----------+

-- select percentage of correctly classified samples (per run and age value)
-- restrict results to those age groups where we have larger than 100 data samples
select Total.ixRun, Total.sValue as age, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue) as Total,
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue and Total.count > 100 order by Total.ixRun, accuracy desc;

+-----------+------+----------+
| ixRun     | age  | accuracy |
+-----------+------+----------+
| 201011231 | 7    |   0.6813 |
| 201011231 | 9    |   0.4932 |
| 201011231 | 8    |   0.4379 |
| 201011231 | 10   |   0.2278 |
| 201011231 | 47   |   0.1333 |
| 201011231 | 14   |   0.1200 |
| 201011231 | 17   |   0.1091 |
| 201011232 | 7    |   0.8969 |
| 201011232 | 9    |   0.6479 |
| 201011232 | 8    |   0.6103 |
| 201011232 | 10   |   0.3926 |
| 201011232 | 47   |   0.3917 |
| 201011232 | 14   |   0.2800 |
| 201011232 | 17   |   0.2182 |
+-----------+------+----------+


-------------------------------------------------------------------------------------------

-- QUESTION 6 What is the accuracy of detection from collected data based on handedness?

select Total.ixRun, Total.sValue as handedness, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, HandData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Handedness') as HandData where HandData.ixData = Result.ixData group by Result.ixRun, HandData.sValue) as Total,
  (select Result.ixRun, HandData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Handedness') as HandData where HandData.ixData = Result.ixData and HandData.sCharacter = Result.sClassification group by Result.ixRun, HandData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+------------+----------+
| ixRun     | handedness | accuracy |
+-----------+------------+----------+
| 201011231 | L          |   0.3778 |
| 201011231 | R          |   0.3487 |
| 201011231 | B          |   0.1000 |
| 201011232 | L          |   0.5556 |
| 201011232 | R          |   0.5195 |
| 201011232 | B          |   0.3000 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 7 Is there a difference in accuracy of the algorithms on data samples visually classified as "good" or "bad" by a team member?

select Total.ixRun, Total.sValue as quality, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, QualityData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Quality') as QualityData where QualityData.ixData = Result.ixData group by Result.ixRun, QualityData.sValue) as Total,
  (select Result.ixRun, QualityData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Quality') as QualityData where QualityData.ixData = Result.ixData and QualityData.sCharacter = Result.sClassification group by Result.ixRun, QualityData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+---------+----------+
| ixRun     | quality | accuracy |
+-----------+---------+----------+
| 201011231 | B       |   0.4932 |
| 201011231 | G       |   0.3264 |
| 201011232 | B       |   0.6159 |
| 201011232 | G       |   0.5061 |
+-----------+---------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 9 Are there any common miscategorizations for sets of digits (such as classifying 6's and 9's as 0's)?


