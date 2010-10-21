--
-- Schema naming conventions adapted from http://www.fogcreek.com/FogBugz/kb/dbsetup/50FogBugzSchema.html
--


CREATE SCHEMA IF NOT EXISTS `Handwriting`;

SHOW WARNINGS ;

USE `Handwriting` ;

-- -----------------------------------------------------
-- Table Handwriting.Source
--
-- Rows contain information about a source institution or website providing handwriting data.
-- -----------------------------------------------------
DROP TABLE IF EXISTS Handwriting.Source ;

SHOW WARNINGS ;

-- ixSource unique integer identifier (primary key)
-- sName name of institution or website providing DataSets
-- sUrl location of institution or website
CREATE TABLE IF NOT EXISTS Handwriting.Source (
  ixSource INT(11)  NOT NULL AUTO_INCREMENT ,
  sName VARCHAR(100) NULL ,
  sUrl VARCHAR(100) NULL ,
  PRIMARY KEY (ixSource) );

SHOW WARNINGS ;

-- -----------------------------------------------------
-- Table Handwriting.DataSet
--
-- Rows contain data about a set of handwriting data.
-- -----------------------------------------------------
DROP TABLE IF EXISTS Handwriting.DataSet ;

SHOW WARNINGS ;

-- ixDataSet unique integer identifier (primary key)
-- ixSource identifier of Source which provided this DataSet (foreign key)
-- sDescription text description of DataSet
-- sUrl location of data files
-- dtAccessTime dat and time DataSet was downloaded and entered into database
CREATE TABLE IF NOT EXISTS Handwriting.DataSet (
  ixDataSet INT(11) NOT NULL AUTO_INCREMENT ,
  ixSource INT(11) NULL ,
  sDescription VARCHAR(100) NULL ,
  sUrl VARCHAR(100) NULL ,
  dtAccessTime DATETIME NULL ,
  PRIMARY KEY (ixDataSet) );

SHOW WARNINGS ;

-- -----------------------------------------------------
-- Table Handwriting.Data
--
-- Rows contain data relating to a single handwriting data sample.
-- -----------------------------------------------------
DROP TABLE IF EXISTS Handwriting.Data ;

SHOW WARNINGS ;

-- ixData unique integer identifier of individual data sample (primary key)
-- ixDataSet identifier of DataSet which provided this Data (foreign key)
-- sCharacter the character or digit represented in the data
-- iRows the number of rows in the bData binary blob
-- iCols the number of columns in the bData binary blob
-- bData a binary blob containing a block of one byte (0 to 255) greyscale pixel values
CREATE TABLE IF NOT EXISTS Handwriting.Data (
  ixData INT(11) NOT NULL AUTO_INCREMENT ,
  ixDataSet INT(11) NOT NULL ,
  sCharacter VARCHAR(1) NOT NULL ,
  iRows INT(11) NOT NULL,
  iCols INT(11) NOT NULL,
  bData BLOB NOT NULL ,
  PRIMARY KEY (ixData) );

SHOW WARNINGS ;

-- -----------------------------------------------------
-- Table Handwriting.Result
--
-- Rows provide freeform key / value pairs for annotating Data with results from algorithmic processing.
-- -----------------------------------------------------
DROP TABLE IF EXISTS Handwriting.Result ;

SHOW WARNINGS ;

-- ixResult unique integer identifier of an in
CREATE TABLE IF NOT EXISTS Handwriting.Result (
  ixResult INT(11) NOT NULL AUTO_INCREMENT ,
  ixData INT(11) NOT NULL ,
  sKey VARCHAR(50) NOT NULL ,
  sValue FLOAT(23) NULL ,
  PRIMARY KEY (ixResult)  );

SHOW WARNINGS ;

CREATE UNIQUE INDEX ResultKeyIndex ON Handwriting.Result (sKey,ixData);

SHOW WARNINGS ;
