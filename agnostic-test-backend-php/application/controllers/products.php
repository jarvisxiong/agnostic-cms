<?php
class Products extends CI_Controller {
	
	function multiplier() {
		
		$this->load->model("prods");
		
		if($this->form_validation->run()) {
			
			$this->prods->apply_price_multiplier($this->input->post("multiplier"));
			redirect("products/multiplier");
		} else {
			$this->load->model("modules");
			$this->load->model("agnostic_session");
			
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