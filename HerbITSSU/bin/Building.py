from interfaces import BuildingType
from org.apache.commons.math.linear import *
import Jama


# import numpy


# Building object that subclasses a Java interface

# Jython Class.
# To use this Class, We need to modify it to use Array only in English
# - Taeheon

class Building(BuildingType):

    def __init__(self):
        self.name = None
        self.address = None
        self.id = -1
        
        
        #var3 = var1 * var2;
       # print cmp(4, 3);
       
 # *****************************************STARTING OF MATRIX MULTIPLYING
 
 
 # Create a real matrix with two rows and three columns
        arrayA = ( [1, 2, 3], [2, 5, 3]);    # a Python arrays
        matrixA = RealMatrixImpl(arrayA);  # convert array to matrix
        print matrixA
        print matrixA.getColumnDimension()    # gets # of columns -> 3
        
        # One more array with three rows, two columns
        arrayB = ( [1, 2], [2, 5], [1, 7]);    # Python array
        matrixB = RealMatrixImpl(arrayB);      # converted to matrix
        print matrixB
        
        # Note: The constructor copies  the input double[][] array.
        
        # Now multiply A x B
        p = matrixA.multiply(matrixB);
        print "p = ", p
        
        print "rows p =", p.getRowDimension();        # 2
        print "columns p = ", p.getColumnDimension(); # 2
        
        
        # Invert p
        pInverse = p.inverse();
        print "Inverse(p) = ", pInverse
        print "Doesn't look nice, does it?"
        print "Let's use Jama for matrix printing ...\n"
        
        # we use Jama to get a nice printing of the matrix
        JmatrixA = Jama.Matrix(arrayA)
        print "matrixA";
        JmatrixA.print(5, 2)    # 4 places, 2 decimals
        
        JmatrixB = Jama.Matrix(arrayB)
        print "matrixB",
        JmatrixB.print(5, 2)    # 4 places, 2 decimals
        
        JpInverse = Jama.Matrix(pInverse.getData())
        print "Inverse(p)",
        JpInverse.print(6, 3)   # 6 places, 3 decimals
        
         
 
 
 # ***************************************** MATRIX MULTIPLYING END

        
        

    def getBuildingName(self):
        return self.name

    def setBuildingName(self, name):
        self.name = name;

    def getBuildingAddress(self):
        return self.address

    def setBuildingAddress(self, address):
        self.address = address

    def getBuildingId(self):
        return self.id

    def setBuildingId(self, id):
        self.id = id