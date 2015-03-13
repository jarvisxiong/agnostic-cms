
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
		
		public void button1Clicked (object sender, EventArgs args)
		{
			button1.Text = "You clicked me";
		}

		public String GetDBData () {
			string connectionString =
				"Server=localhost;" +
					"Database=agnosticcms;" +
					"Port=5433;" +
					"User ID=postgres;" +
					"Password=postgres;";
			IDbConnection dbcon;
			dbcon = new NpgsqlConnection(connectionString);
			dbcon.Open();
			IDbCommand dbcmd = dbcon.CreateCommand();
			// requires a table to be created named employee
			// with columns firstname and lastname
			// such as,
			//        CREATE TABLE employee (
			//           firstname varchar(32),
			//           lastname varchar(32));
			string sql = "SELECT m.id, m.name, mc.id, mc.name, mc.name, mc.id" +
										"FROM cms_modules m JOIN cms_module_columns mc" +
										"ON mc.cms_module_id = m.id";
			dbcmd.CommandText = sql;
			NpgsqlDataReader reader = (Npgsql.NpgsqlDataReader) dbcmd.ExecuteReader();

			Category curCategory = null;
			while(reader.Read()) {
				long categoryId = reader.GetInt64(0);
				string categoryName = reader.GetString(1);
				long productId = reader.GetInt64(2);
				string productName = reader.GetString(3);
				string productImage = reader.GetString(4);
				decimal productPrice = reader.GetDecimal(5);

				if(curCategory == null || curCategory.id != categoryId) {
					curCategory = new Category ();
					curCategory.id = categoryId;
					curCategory.name = categoryName;
				}

				Product product = new Product ();
				product.id = productId;
				product.name = productName;
				product.image = productImage;
				product.price = productPrice;
				curCategory.addProcuct (product);
			}
			// clean up
			reader.Close();
			reader = null;
			dbcmd.Dispose();
			dbcmd = null;
			dbcon.Close();
			dbcon = null;

			return "aaa";
		}
	}

	public class Category {
		public long id { get; set;}
		public string name { get; set;}
		public List<Product> products { get; set;}

		public Category() {
			products = new List<Product>(); 
		}

		public void addProcuct(Product product) {
			products.Add(product);
		}
 	}

	public class Product {
		public long id { get; set;}
		public string name { get; set;}
		public string image { get; set;}
		public decimal price { get; set;}
	}
}

