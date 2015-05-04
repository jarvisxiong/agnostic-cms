<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');


if ( ! function_exists('preprocess_decimal'))
{
	/**
	 * Formats input decimal by replacing "," with "." and adding decimal part, if not already present
	 * @param string $input Input decimal number
	 * @return string Formatted decimal number
	 */
	function preprocess_decimal($input) {
		$input = str_replace(",", ".", $input);
		if(strpos($input, '.') === FALSE) {
			$input .= '.0';
		}
	
		return $input;
	}
}