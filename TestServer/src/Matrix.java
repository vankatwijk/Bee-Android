
import java.io.Serializable;

public class Matrix implements Serializable{
	int id;
	String name;
	int nrows;	
	int ncols;

	double[][] values;

	public Matrix(int id, String name){
		this.id = id;
		this.name = name;
		this.nrows = 2;
		this.ncols = 2;
		values = new double[nrows][ncols];
	}
	
	public Matrix(int id, String name, int nrows, int ncols){
		this.id = id;
		this.name = name;
		this.nrows = nrows;
		this.ncols = ncols;
		values = new double[nrows][ncols];
	}

	public Matrix(int id, String name, int nrows, int ncols, double[][] values){
		this.id = id;
		this.name = name;
		this.nrows = nrows;
		this.ncols = ncols;
		this.values = values;
	}
	
	public Matrix(int id, String name, int nrows, int ncols, String valuesString){
		this.id = id;
		this.name = name;
		this.values = new double[nrows][ncols];
		String[] rows = valuesString.split(";");
		String[] cols = null; 
		for(int r=0; r<rows.length; r++){
			cols = rows[r].split(",");
			for(int c=0; c<cols.length; c++){
				this.values[r][c] = Double.parseDouble(cols[c]);
			}
		}
		this.nrows = rows.length;
		this.ncols = cols.length;
	}

	public String getName(){
		return name;
	}
	
	public String getSizeString(){
		return nrows + "x"+ncols;
	}
	
	public String toString(){
		//String strValues = getStringValues();
		return name;// + " (" + nrows + ","+ncols+"):";// + strValues;
	}
	
	public String getStringValues(){
		String strValues ="";
		for(int r=0; r<nrows; r++){
			for(int c=0; c<ncols; c++){
				strValues += values[r][c]+",";
			}
			strValues += ";";
		}
		return strValues;
	}
	
	public Matrix add(Matrix a){
		Matrix result = null;
		if(this.nrows==a.nrows && this.ncols == a.ncols){
			double[][] res = new double[nrows][ncols];
			for(int r=0; r<nrows; r++){
				for(int c=0; c<ncols; c++){
					res[r][c] = values[r][c]+a.values[r][c];
				}
			}
			result = new Matrix(0, "R", nrows, ncols, res);			
		}
		return result;
	}
	
	public Matrix substract(Matrix a){
		Matrix result = null;
		if(this.nrows==a.nrows && this.ncols == a.ncols){
			double[][] res = new double[nrows][ncols];
			for(int r=0; r<nrows; r++){
				for(int c=0; c<ncols; c++){
					res[r][c] = values[r][c]-a.values[r][c];
				}
			}
			result = new Matrix(0, "R", nrows, ncols, res);			
		}
		return result;
	}
	
	public Matrix multiply(Matrix a){
		Matrix result = null;
		if(this.ncols==a.nrows){
			double[][] res = new double[this.nrows][a.ncols];
			for(int r=0; r<this.nrows; r++){
				for(int c=0; c<a.ncols; c++){
					res[r][c] = 0;
					for(int i=0; i<this.ncols; i++){
						res[r][c] = res[r][c] + this.values[r][i]*a.values[i][c];
					}
				}
			}
			result = new Matrix(0, "R", this.nrows, a.ncols, res);			
		}
		return result;
	}
	
	public Matrix scalarAddition(double a){
		double[][] res = new double[nrows][ncols];
		for(int r=0; r<nrows; r++){
			for(int c=0; c<ncols; c++){
				res[r][c] = a+values[r][c];
			}
		}
		Matrix result = new Matrix(0, "R", nrows, ncols, res);		
		return result;
	}
	
	public Matrix scalarSubstraction(double a){
		double[][] res = new double[nrows][ncols];
		for(int r=0; r<nrows; r++){
			for(int c=0; c<ncols; c++){
				res[r][c] = a-values[r][c];
			}
		}
		Matrix result = new Matrix(0, "R", nrows, ncols, res);		
		return result;
	}
	
	public Matrix scalarMultiplication(double a){
		double[][] res = new double[nrows][ncols];
		for(int r=0; r<nrows; r++){
			for(int c=0; c<ncols; c++){
				res[r][c] = a*values[r][c];
			}
		}
		Matrix result = new Matrix(0, "R", nrows, ncols, res);		
		return result;
	}
	

}
