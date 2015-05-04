<?php
class Products extends CI_Controller {
	
	/**
	 * Main controller for product price multiplication
	 */
	function multiplier() {
		
		$this->load->model("prods");
		
		// If form validation successful - multiply the prices and redirect back to main view
		if($this->form_validation->run()) {
			
			$this->prods->apply_price_multiplier($this->input->post("multiplier"));
			redirect("products/multiplier");
		
		// If form validation unsuccessful or no post data present - show the main view
		} else {
			$this->load->model("modules");
			$this->load->model("agnostic_session");
			
			// Add data to view for displaying
			$data = array(
					'username' => $this->agnostic_session->get_attribute("cms-user"),
					'external_modules' => $this->modules->get_external_modules(),
					'modules' => $this->modules->get_modules(),
					'selected_external_module_id' => 1,
					'products' => $this->prods->get_all_products()
			);
			$this->load->view("main", $data);
		}
		
	}

}