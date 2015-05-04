<?php
class Prods extends CI_Model {
	
	/**
	 * Multiplies price of all products by the given number
	 * @param double $multiplier The multiplier number
	 */
	function apply_price_multiplier($multiplier) {
		$this->db->query("UPDATE products SET price = price * ?", array($multiplier));
	}
	
	/**
	 * Selects all products from database
	 * @return array Array of all products form database
	 */
	function get_all_products() {
		$this->db->select('*');
		$this->db->from("products");
		$this->db->order_by("order_num");
		$query = $this->db->get();
		return $query->result();
	}
	
}