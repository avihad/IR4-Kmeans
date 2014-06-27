import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class k_mean {


	double [][] distMap;
	ArrayList<sparse_vec> vectors  ;
	ArrayList<Integer> centroids = new ArrayList<Integer> () ; 
	ArrayList<ArrayList<Integer>> membership= new ArrayList<ArrayList<Integer>>();

	public k_mean(ArrayList<sparse_vec> vectors) throws InterruptedException {
		super();
		this.vectors = vectors;
		this.calc_dist_mat(vectors);
	}

	public  void calc_k_mean ( int k){

		Double tmp;
		for (int i=0;i<k;i++){
			tmp= Math.random()*vectors.size();
			while (centroids.contains(tmp))
				tmp= Math.random()*vectors.size();
				centroids.add(  tmp.intValue());
		}
		for (int i=0;i<k;i++)
			membership.add(new ArrayList<Integer>());


		int move = vectors.size();
		while (move>(0.1*vectors.size())){
			

			move= recalc_membersip();
			recalc_centroids();
			
			System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
		}
	}

	public  static double dist (sparse_vec a,sparse_vec b){
		double sum=0;
		Set<Integer> set = new HashSet<Integer>(a.vec.keySet());
		set.addAll(b.vec.keySet());
		for(Integer i :set){
			sum+= Math.pow(a.get(i)-b.get(i),2);
		}
		return Math.sqrt(sum);
	}

	public  void calc_dist_mat (ArrayList<sparse_vec> vectors) throws InterruptedException{
		ExecutorService executor = Executors.newFixedThreadPool(8);

		int size = vectors.size();
		double [][] mat = new  double [size][size];
		for (int i=0;i<size;i++){
			Runnable worker = new line_calculator(i,mat,vectors);
			 executor.execute(worker);
	//		for (int j=0;j<=i;j++)
	//			mat[i][j]=dist(vectors.get(i),vectors.get(j));
		}
		executor.shutdown();
	     while (!executor.isTerminated()) {
	    	 Thread.sleep(10000);
	    	         }
	    	 
	    	         System.out.println("Finished all threads");

		for (int i=0;i<size;i++){
			for (int j=i;j<size;j++)
				mat[i][j]=mat[j][i];
		}

		this.distMap=mat;
	}
	
	public  void add (sparse_vec a, sparse_vec b){
		Set<Integer> set = a.vec.keySet();
		set.addAll(b.vec.keySet());
		for(Integer i :set){
			a.set(i, a.get(i)+b.get(i));
		}
		
	}
	
	public  void div (sparse_vec a, double b){
		Set<Integer> set = a.vec.keySet();
		for(Integer i :set){
			a.set(i, a.get(i)/b);
		}
		
	}




	public  int recalc_membersip (){
		int tmp;
		int change = 0;
		ArrayList<ArrayList<Integer>> NewMembership = new ArrayList<ArrayList<Integer>>();
		for (int i=0;i<membership.size();i++)
			NewMembership.add(new ArrayList<Integer>());

		for (int i=0;i< vectors.size();i++){
			tmp = find_closest_centroid(i);
			if (!membership.get(tmp).contains(i)){
				change ++;
			}
			NewMembership.get(tmp).add(i);
			
		}
		this.membership=NewMembership;
		return change;


	}
	public  void recalc_centroids (){
	
	

		for (int i=0;i<this.membership.size();i++){
			
			double dist = Integer.MAX_VALUE;
			for (int j=0;j<vectors.size();j++){
				double tmp_dist =0;
				
				for (int z =0; z< membership.get(i).size() ;z++ ){
					tmp_dist +=this.distMap[membership.get(i).get(z)][j];
				}
				if (tmp_dist <dist)
				{
					dist = tmp_dist;
					this.centroids.set(i, j);
				}

			}
		}


	}

	public  int find_closest_centroid (int j){
		double dist = Double.MAX_VALUE;
		int ans =0;
		double tmp_dist;
		for(int i=0;i<centroids.size();i++){
			tmp_dist =this.distMap[j][centroids.get(i)]; 
			if (tmp_dist<dist){
				dist = tmp_dist;
				ans= i;
			}

		}
		return ans;
	}

}
