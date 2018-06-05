# Data Pre-processing

# Importing libraries
import pandas as pd
import numpy as np

# Importing the data set
df = pd.read_csv('C:/UPB/SS_2018/Intelligent_Assistant/Panama_Papers/Entities.csv', dtype='unicode')

# Taking care of missing data 

def num_missing(x):
    return sum(x.isnull())

# Applying per column:
print("\n Missing values per column:")
# axis=0 defines that function is to be applied on each column
print(df.apply(num_missing, axis=0))

# Applying per row:
print("\n Missing values per row:")
# axis=1 defines that function is to be applied on each row
print(df.apply(num_missing).head())

# Encoding the categorical data and filling the missing values 

from sklearn.base import TransformerMixin

X = pd.DataFrame(df)
class DataFrameImputer(TransformerMixin):

    def __init__(values):
        """Impute missing values.

        Columns of dtype object are imputed with the most frequent value 
        in column.

        Columns of other types are imputed with mean of column.

        """
    def fit(values, X):

        values.fill = pd.Series([X[c].value_counts().index[0]
            if X[c].dtype == np.dtype('O') else X[c].mean() for c in X],
            index=X.columns)

        return values

    def transform(values, X):
        return X.fillna(values.fill)


xt = DataFrameImputer().fit_transform(X)
xt.tail() # displaying the last 5 transfromed row values

		
		
