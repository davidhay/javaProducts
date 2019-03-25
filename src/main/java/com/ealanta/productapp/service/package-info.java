/**
 * The ProductSource takes a ProductItemsSource which supplies ProductItems. It then converts each raw ProductItem
 * into a Product (which holds numeric values in way they can be used). It then filters this Product list to
 * remove any Products that don't have a price reduction and sorts them from greatest price reduction
 * to lowest.
 */
package com.ealanta.productapp.service;