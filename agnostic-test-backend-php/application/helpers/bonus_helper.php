<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');


if ( ! function_exists('preprocess_decimal'))
{
	function preprocess_decimal($input) {
		$input = str_replace(",", ".", $input);
		if(strpos($input, '.') === FALSE) {
			$input .= '.0';
		}
	
		return $input;
	}
}