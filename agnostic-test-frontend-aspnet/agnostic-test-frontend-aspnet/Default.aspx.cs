
namespace agnostictestfrontend
{
	using System;
	using System.Data;
	using System.Web;
	using System.Web.UI;
	using System.Collections.Generic;
	using Npgsql;

	public partial class Default : System.Web.UI.Page
	{

		// Disclaimer: This function contains naive c# code, ment just for illustrating how frontend can be built
		// in the Agnostic CMS deployment.
		// Returns a list of all categories created in Agnostic CMS and their belonging products
		public List<Category> GetDBData () {
			// String for connecting the database
			string connectionString =
				"Server=localhost;" +
					"Database=agnosticcms;" +
					"Port=5433;" +
					"User ID=postgres;" +
					"Password=postgres;";

			// Naive implementation of database connection handling - new connection for each request
			IDbConnection dbcon;
			dbcon = new NpgsqlConnection(connectionString);
			dbcon.Open();
			IDbCommand dbcmd = dbcon.CreateCommand();

			string sql = "SELECT pc.id, pc.name, pc.description, p.id, p.name, p.description, p.image, p.price " +
										"FROM product_categories pc JOIN products p " +
										"ON p.product_category_id = pc.id " +
										"ORDER BY pc.id, p.id";
			dbcmd.CommandText = sql;
			// Executing the sql query and returning rows of products and categories they belong to
			NpgsqlDataReader reader = (Npgsql.NpgsqlDataReader) dbcmd.ExecuteReader();

			List<Category> categories = new List<Category>();
			// Current categoty in question
			Category curCategory = null;
			while(reader.Read()) {
				long categoryId = reader.GetInt64(0);
				string categoryName = reader.GetString(1);
				string categoryDescription = reader.GetString(2);
				long productId = reader.GetInt64(3);
				string productName = reader.GetString(4);
				string productDescription = reader.GetString(5);
				string productImage = reader.GetString(6);
				decimal productPrice = (decimal) reader.GetDouble(7);

				// As rows are ordered by category ids, we can create a new one only at time
				// when the id changes
				if(curCategory == null || curCategory.id != categoryId) {
					curCategory = new Category ();
					categories.Add (curCategory);
					curCategory.id = categoryId;
					curCategory.name = categoryName;
					curCategory.description = categoryDescription;
				}

				Product product = new Product ();
				product.id = productId;
				product.name = productName;
				product.description = productDescription;
				product.image = productImage;
				product.price = productPrice;

				// Adding product to the current category in question
				curCategory.addProcuct (product);
			}
			// Close the connection
			reader.Close();
			reader = null;
			dbcmd.Dispose();
			dbcmd = null;
			dbcon.Close();
			dbcon = null;

			return categories;
		}
	}

	// Class for category and it's products from the Agnostic CMS database 
	public class Category {
		public long id { get; set;}
		public string name { get; set;}
		public string description { get; set;}
		public List<Product> products { get; set;}

		public Category() {
			products = new List<Product>(); 
		}

		public void addProcuct(Product product) {
			products.Add(product);
		}
 	}

	// Class for products from the Agnostic CMS database 
	public class Product {
		public long id { get; set;}
		public string description { get; set;}
		public string name { get; set;}
		public string image { get; set;}
		public decimal price { get; set;}
	}
}

