using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
using Newtonsoft.Json;
using System.Text.RegularExpressions;

namespace _CRUD_Agenda
{
    public partial class CRUD_AGENDA : Form
    {
        // Constante global para la base de la URL
        private const string BASE_URL = "http://localhost:8000/index.php";

        // HttpClient global reutilizable
        private static readonly HttpClient httpClient = new HttpClient();

        public CRUD_AGENDA()
        {
            InitializeComponent();
            Task task = btnConsulta();

            textBox1.Enabled = false; // ID no se edita
        }

        private void label3_Click(object sender, EventArgs e) { }
        private void label1_Click(object sender, EventArgs e) { }
        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e) { }

        private async void button1_Click(object sender, EventArgs e)
        {
            await btnCrear();
        }

        public async Task btnConsulta()
        {
            string url = $"{BASE_URL}?tipo=1";

            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(url);
                response.EnsureSuccessStatusCode();
                string result = await response.Content.ReadAsStringAsync();
                Root datos = JsonConvert.DeserializeObject<Root>(result);

                this.dataGridView1.Rows.Clear();

                // Columnas del DataGridView (por cómo las tienes en el diseñador):
                // 0: id, 1: nombre, 2: apellidos, 3: telefono, 4: EMAIL, 5: CLAVE
                for (int i = 0; i < datos.dato.Count; i++)
                {
                    this.dataGridView1.Rows.Add(
                        datos.dato[i].id,
                        datos.dato[i].nombre,
                        datos.dato[i].apellidos,
                        datos.dato[i].telefono,
                        datos.dato[i].email, // col 4 = email
                        datos.dato[i].clave  // col 5 = clave
                    );
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al consultar: {ex.Message}");
            }
        }

        // ---- VALIDACIONES ----

        private bool ValidarCamposBasicos()
        {
            string nombre = textBox2.Text.Trim();
            string apellidos = textBox3.Text.Trim();
            string telefono = textBox4.Text.Trim();
            string email = textBox7.Text.Trim(); // email en textBox7

            if (string.IsNullOrWhiteSpace(nombre))
            {
                MessageBox.Show("El campo 'Nombre' es obligatorio.");
                textBox2.Focus();
                return false;
            }

            if (string.IsNullOrWhiteSpace(apellidos))
            {
                MessageBox.Show("El campo 'Apellidos' es obligatorio.");
                textBox3.Focus();
                return false;
            }

            if (string.IsNullOrWhiteSpace(telefono))
            {
                MessageBox.Show("El campo 'Teléfono' es obligatorio.");
                textBox4.Focus();
                return false;
            }

            if (!telefono.All(char.IsDigit))
            {
                MessageBox.Show("El teléfono solo debe contener números.");
                textBox4.Focus();
                return false;
            }

            if (telefono.Length < 7 || telefono.Length > 15)
            {
                MessageBox.Show("El teléfono debe tener entre 7 y 15 dígitos.");
                textBox4.Focus();
                return false;
            }

            if (string.IsNullOrWhiteSpace(email))
            {
                MessageBox.Show("El campo 'Email' es obligatorio.");
                textBox7.Focus();
                return false;
            }

            // Validación simple de email
            string pattern = @"^[^@\s]+@[^@\s]+\.[^@\s]+$";
            if (!Regex.IsMatch(email, pattern))
            {
                MessageBox.Show("El email no tiene un formato válido.");
                textBox7.Focus(); // <- aquí antes tenías textBox5
                return false;
            }

            return true;
        }

        private bool ValidarId()
        {
            if (string.IsNullOrWhiteSpace(textBox1.Text))
            {
                MessageBox.Show("Selecciona un registro de la tabla primero.");
                return false;
            }
            return true;
        }

        private bool ValidarClaveParaModificar()
        {
            string clave = textBox5.Text.Trim(); // clave en textBox5
            if (string.IsNullOrWhiteSpace(clave))
            {
                MessageBox.Show("No se encontró la clave del registro. Vuelve a seleccionar el registro en la tabla.");
                return false;
            }
            return true;
        }

        // ---- CRUD ----

        public async Task btnCrear()
        {
            if (!ValidarCamposBasicos())
                return;

            string nom = textBox2.Text.Trim();
            string app = textBox3.Text.Trim();
            string tel = textBox4.Text.Trim();
            string email = textBox7.Text.Trim();
            string clave = textBox5.Text.Trim();

            // URL encode de parámetros
            string nomEnc = Uri.EscapeDataString(nom);
            string appEnc = Uri.EscapeDataString(app);
            string telEnc = Uri.EscapeDataString(tel);
            string emailEnc = Uri.EscapeDataString(email);
            string claveEnc = Uri.EscapeDataString(clave);

            string url = $"{BASE_URL}?tipo=2&nombre={nomEnc}&apellidos={appEnc}&telefono={telEnc}&email={emailEnc}&clave={claveEnc}";

            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(url);
                response.EnsureSuccessStatusCode();
                string result = await response.Content.ReadAsStringAsync();
                await btnConsulta();
                LimpiarDatos();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al crear: {ex.Message}");
            }
        }

        public async Task btnEliminar()
        {
            if (!ValidarId())
                return;

            string id = textBox1.Text.Trim();
            string idEnc = Uri.EscapeDataString(id);

            string url = $"{BASE_URL}?tipo=4&id={idEnc}";

            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(url);
                response.EnsureSuccessStatusCode();
                string result = await response.Content.ReadAsStringAsync();
                await btnConsulta();
                LimpiarDatos();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al eliminar: {ex.Message}");
            }
        }

        public async Task btnModificar()
        {
            if (!ValidarId())
                return;

            if (!ValidarCamposBasicos())
                return;

            if (!ValidarClaveParaModificar())
                return;

            string id = textBox1.Text.Trim();
            string nom = textBox2.Text.Trim();
            string app = textBox3.Text.Trim();
            string tel = textBox4.Text.Trim();
            string email = textBox7.Text.Trim();
            string clave = textBox5.Text.Trim(); // clave en textBox5

            // URL encode de parámetros
            string idEnc = Uri.EscapeDataString(id);
            string nomEnc = Uri.EscapeDataString(nom);
            string appEnc = Uri.EscapeDataString(app);
            string telEnc = Uri.EscapeDataString(tel);
            string emailEnc = Uri.EscapeDataString(email);
            string claveEnc = Uri.EscapeDataString(clave);

            string url = $"{BASE_URL}?tipo=3&nombre={nomEnc}&apellidos={appEnc}&telefono={telEnc}&email={emailEnc}&clave={claveEnc}&id={idEnc}";

            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(url);
                response.EnsureSuccessStatusCode();
                string result = await response.Content.ReadAsStringAsync();
                await btnConsulta();
                LimpiarDatos();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al modificar: {ex.Message}");
            }
        }

        private async void button3_Click(object sender, EventArgs e)
        {
            await btnEliminar();
        }

        private async void button2_Click(object sender, EventArgs e)
        {
            await btnModificar();
        }

        private void dataGridView1_DoubleClick(object sender, EventArgs e)
        {
            int x = dataGridView1.CurrentCell.RowIndex;

            // Cargamos los campos desde la fila seleccionada
            this.textBox1.Text = dataGridView1.Rows[x].Cells[0].Value?.ToString(); // id
            this.textBox2.Text = dataGridView1.Rows[x].Cells[1].Value?.ToString(); // nombre
            this.textBox3.Text = dataGridView1.Rows[x].Cells[2].Value?.ToString(); // apellidos
            this.textBox4.Text = dataGridView1.Rows[x].Cells[3].Value?.ToString(); // telefono

            // email (columna 4)
            if (dataGridView1.Rows[x].Cells.Count > 4 &&
                dataGridView1.Rows[x].Cells[4].Value != null)
            {
                this.textBox7.Text = dataGridView1.Rows[x].Cells[4].Value.ToString();
            }
            else
            {
                this.textBox7.Clear();
            }

            // clave (columna 5)
            if (dataGridView1.Rows[x].Cells.Count > 5 &&
                dataGridView1.Rows[x].Cells[5].Value != null)
            {
                this.textBox5.Text = dataGridView1.Rows[x].Cells[5].Value.ToString();
            }
            else
            {
                this.textBox5.Clear();
            }
        }

        private void LimpiarDatos()
        {
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear(); // clave
            textBox7.Clear(); // email
        }

        private async void button4_Click(object sender, EventArgs e)
        {
            await btnConsulta();
        }

        private void textBox5_TextChanged(object sender, EventArgs e) { }
        private void label5_Click(object sender, EventArgs e) { }
        private void CRUD_AGENDA_Load(object sender, EventArgs e) { }
        private void textBox7_TextChanged(object sender, EventArgs e) { }

        private void button4_Click_1(object sender, EventArgs e)
        {
            LimpiarDatos();
        }
    }
}

// Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse);
public class Dato
{
    public string id { get; set; }
    public string nombre { get; set; }
    public string apellidos { get; set; }
    public string telefono { get; set; }
    public string clave { get; set; }
    public string email { get; set; }
}

public class Root
{
    public List<Dato> dato { get; set; }
}
