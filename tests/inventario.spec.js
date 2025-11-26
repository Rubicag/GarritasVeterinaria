const { test, expect } = require('@playwright/test');

test.describe('Inventario UI', () => {
  test.beforeEach(async ({ page }) => {
    // Assumes local server is running at baseURL
    await page.goto('/inventario');
    await expect(page).toHaveTitle(/Inventario|Garritas Veterinaria/);
  });

  test('Ver detalles abre modal', async ({ page }) => {
    // wait for table and first verProducto button
    await page.waitForSelector('#tablaInventario');
    const btn = await page.locator('#tablaInventario .btn-accion[data-action="verProducto"]').first();
    await expect(btn).toBeVisible();
    await btn.click();
    // modal should appear
    const modal = page.locator('#verProductoModal');
    await expect(modal).toBeVisible();
  });

  test('Buscar producto filtra la tabla', async ({ page }) => {
    await page.waitForSelector('#tablaInventario');
    // Type a term known to exist in seed data like 'Alimento' (adjust if needed)
    await page.fill('#buscarProducto', 'Alimento');
    await page.click('#btnBuscar');
    // Wait for DataTable redraw
    await page.waitForTimeout(500);
    const rows = await page.locator('#tablaInventario tbody tr:visible');
    await expect(rows).toHaveCountGreaterThan(0);
    const firstText = await rows.nth(0).textContent();
    expect(firstText.toLowerCase()).toContain('alimento');
  });
});
