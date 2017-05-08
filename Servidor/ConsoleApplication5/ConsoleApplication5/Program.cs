using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using Newtonsoft.Json;
using System.Linq;
using System.Data.SqlClient;

namespace ConsoleApplication5
{
    public class StateObject
    {
        // Client  socket.  
        public Socket workSocket = null;
        // Size of receive buffer.  
        public const int BufferSize = 1024;
        // Receive buffer.  
        public byte[] buffer = new byte[BufferSize];
        // Received data string.  
        public StringBuilder sb = new StringBuilder();
    }
    public class user
    {
        public string nome_completo { get; set; }
        public string cpf { get; set; }
        public string rg { get; set; }
        public string email { get; set; }
        public string celular { get; set; }
        public string telefone { get; set; }
        public string usuario { get; set; }
        public string senha { get; set; }
    }

    public class motorista
    {
        public string nome { get; set; }
        public string cpf { get; set; }
        public string rg { get; set; }
        public string email { get; set; }
        public string celular { get; set; }
        public string telefone { get; set; }
        public string usuario { get; set; }
        public string senha { get; set; }
    }
    public class distribuidor
    {
        public string razao_social { get; set; }
        public string nome_fantasia { get; set; }
        public string cnpj { get; set; }
        public string inscricao_estadual { get; set; }
        public string pais { get; set; }
        public string estado { get; set; }
        public string cidade { get; set; }
        public string endereco { get; set; }
        public string bairro { get; set; }
        public string cep { get; set; }
        public string complemento { get; set; }
        public string email { get; set; }
        public string senha { get; set; }
        public string telefone { get; set; }
        public string celular { get; set; }
        public string site { get; set; }
        public string nome_contato { get; set; }
        
    }

    public class automovel
    {
        public string username { get; set; }
        public int tipoV { get; set; }
        public int tipoC { get; set; }
        public int carga { get; set; }
        public string placa { get; set; }
        public string nome { get; set; }
        public int rast { get; set; }
        public string marca { get; set; }
        public string modelo { get; set; }
        public int ano { get; set; }
        public string cor { get; set; }
    }

    class Program
    {
        //public static List<String> totalItems = new List<string>();
        public static List<String> user = new List<string>();
        public static List<String> autoInfo = new List<string>();
        public static ManualResetEvent allDone = new ManualResetEvent(false);
        public static bool updateDBAutomoveis()
        {
            try
            {
                String connectionString = Properties.Settings.Default.Fretinga;
                SqlConnection sql = new SqlConnection(connectionString);
                sql.Open();

                SqlCommand readCmd = new SqlCommand("SELECT COUNT(*) FROM automoveis", sql);
                SqlDataReader reader = readCmd.ExecuteReader();
                reader.Read();
                int size = reader.GetInt32(0);
                Console.WriteLine(size.ToString());
                reader.Close();

                using (SqlCommand cmd = new SqlCommand("INSERT INTO automoveis VALUES(@id, @user, @tipo, @carroceria, @carga, @placa, @nome, @rastreador, @marca, @modelo, @ano, @cor)", sql))
                {
                    cmd.Parameters.Add("@id", System.Data.SqlDbType.Int).Value = size;
                    cmd.Parameters.Add("@user", System.Data.SqlDbType.VarChar).Value = autoInfo[0];
                    cmd.Parameters.Add("@tipo", System.Data.SqlDbType.Int).Value = Int32.Parse(autoInfo[1]);
                    cmd.Parameters.Add("@carroceria", System.Data.SqlDbType.Int).Value = Int32.Parse(autoInfo[2]);
                    cmd.Parameters.Add("@carga", System.Data.SqlDbType.Int).Value = Int32.Parse(autoInfo[3]);
                    cmd.Parameters.Add("@placa", System.Data.SqlDbType.VarChar).Value = autoInfo[4];
                    cmd.Parameters.Add("@nome", System.Data.SqlDbType.VarChar).Value = autoInfo[5];
                    cmd.Parameters.Add("@rastreador", System.Data.SqlDbType.Int).Value = Int32.Parse(autoInfo[6]);
                    cmd.Parameters.Add("@marca", System.Data.SqlDbType.VarChar).Value = autoInfo[7];
                    cmd.Parameters.Add("@modelo", System.Data.SqlDbType.VarChar).Value = autoInfo[8];
                    cmd.Parameters.Add("@ano", System.Data.SqlDbType.Int).Value = Int32.Parse(autoInfo[9]);
                    cmd.Parameters.Add("@cor", System.Data.SqlDbType.VarChar).Value = autoInfo[10];
                    cmd.ExecuteNonQuery();
                }
                sql.Close();
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                return false;
            }


        }

        public static bool updateDBUsers()
        {
            try
            {
                String connectionString = Properties.Settings.Default.Fretinga;
                SqlConnection sql = new SqlConnection(connectionString);
                sql.Open();

                SqlCommand readCmd = new SqlCommand("SELECT COUNT(*) FROM usuarios", sql);
                SqlDataReader reader = readCmd.ExecuteReader();
                reader.Read();
                int size = reader.GetInt32(0)+1;
                Console.WriteLine(size.ToString());
                reader.Close();

                using (SqlCommand cmd = new SqlCommand("INSERT INTO usuarios VALUES(@id, @user, @email, @pass, @tipo, @cpf_cnpj, @rg, @data, @nome, @foto)", sql))
                {
                    cmd.Parameters.Add("@id", System.Data.SqlDbType.Int).Value = size;
                    cmd.Parameters.Add("@user", System.Data.SqlDbType.Text).Value = user[1];
                    cmd.Parameters.Add("@email", System.Data.SqlDbType.Text).Value = user[2];
                    cmd.Parameters.Add("@pass", System.Data.SqlDbType.Text).Value = user[3];
                    cmd.Parameters.Add("@tipo", System.Data.SqlDbType.Text).Value = user[4];
                    cmd.Parameters.Add("@cpf_cnpj", System.Data.SqlDbType.Text).Value = user[5];
                    cmd.Parameters.Add("@rg", System.Data.SqlDbType.Text).Value = user[6];
                    cmd.Parameters.Add("@data", System.Data.SqlDbType.Text).Value = user[7];
                    cmd.Parameters.Add("@nome", System.Data.SqlDbType.Text).Value = user[8];
                    cmd.Parameters.Add("@foto", System.Data.SqlDbType.Image, 10).Value = new byte[10];
                    cmd.ExecuteNonQuery();
                }
                sql.Close();
                return true;
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return false;
            }

            
        }
        public static bool adicionarMotorista()
        {
            try
            {
                String connectionString = Properties.Settings.Default.Fretinga;
                SqlConnection sql = new SqlConnection(connectionString);
                sql.Open();

                SqlCommand readCmd = new SqlCommand("SELECT COUNT(*) FROM table_motorista", sql);
                SqlDataReader reader = readCmd.ExecuteReader();
                reader.Read();
                int size = reader.GetInt32(0) + 1;
                Console.WriteLine(size.ToString());
                reader.Close();

                using (SqlCommand cmd = new SqlCommand("INSERT INTO table_motorista VALUES(@id, @nome, @cpf, @rg, @email, @celular, @telefone, @usuario, @senha, @foto)", sql))
                {
                    cmd.Parameters.Add("@id", System.Data.SqlDbType.Int).Value = size;
                    cmd.Parameters.Add("@nome", System.Data.SqlDbType.Text).Value = user[0];
                    cmd.Parameters.Add("@cpf", System.Data.SqlDbType.Text).Value = user[1];
                    cmd.Parameters.Add("@rg", System.Data.SqlDbType.Text).Value = user[2];
                    cmd.Parameters.Add("@email", System.Data.SqlDbType.Text).Value = user[3];
                    cmd.Parameters.Add("@celular", System.Data.SqlDbType.Text).Value = user[4];
                    cmd.Parameters.Add("@telefone", System.Data.SqlDbType.Text).Value = user[5];
                    cmd.Parameters.Add("@usuario", System.Data.SqlDbType.Text).Value = user[6];
                    cmd.Parameters.Add("@senha", System.Data.SqlDbType.Text).Value = user[7];
                    cmd.Parameters.Add("@foto", System.Data.SqlDbType.Image, 10).Value = new byte[10];
                    cmd.ExecuteNonQuery();
                }
                sql.Close();
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                return false;
            }


        }
        public static bool adicionarDistribuidor()
        {
            try
            {
                String connectionString = Properties.Settings.Default.Fretinga;
                SqlConnection sql = new SqlConnection(connectionString);
                sql.Open();

                SqlCommand readCmd = new SqlCommand("SELECT COUNT(*) FROM table_distribuidor", sql);
                SqlDataReader reader = readCmd.ExecuteReader();
                reader.Read();
                int size = reader.GetInt32(0) + 1;
                Console.WriteLine(size.ToString());
                reader.Close();

                using (SqlCommand cmd = new SqlCommand("INSERT INTO table_distribuidor VALUES(@id, @razao_social, @nome_fantasia, @cnpj, @inscricao_estadual, @pais, @estado, @cidade, @endereco, @bairro, @cep, @complemento, @email, @senha, @telefone, @celular, @site, @nome_contato)", sql))
                {
                    cmd.Parameters.Add("@id", System.Data.SqlDbType.Int).Value = size;
                    cmd.Parameters.Add("@razao_social", System.Data.SqlDbType.Text).Value = user[0];
                    cmd.Parameters.Add("@nome_fantasia", System.Data.SqlDbType.Text).Value = user[1];
                    cmd.Parameters.Add("@cnpj", System.Data.SqlDbType.Text).Value = user[2];
                    cmd.Parameters.Add("@inscricao_estadual", System.Data.SqlDbType.Text).Value = user[3];
                    cmd.Parameters.Add("@pais", System.Data.SqlDbType.Text).Value = user[4];
                    cmd.Parameters.Add("@estado", System.Data.SqlDbType.Text).Value = user[5];
                    cmd.Parameters.Add("@cidade", System.Data.SqlDbType.Text).Value = user[6];
                    cmd.Parameters.Add("@endereco", System.Data.SqlDbType.Text).Value = user[7];
                    cmd.Parameters.Add("@bairro", System.Data.SqlDbType.Text).Value = user[8];
                    cmd.Parameters.Add("@cep", System.Data.SqlDbType.Text).Value = user[9];
                    cmd.Parameters.Add("@complemento", System.Data.SqlDbType.Text).Value = user[10];
                    cmd.Parameters.Add("@email", System.Data.SqlDbType.Text).Value = user[11];
                    cmd.Parameters.Add("@senha", System.Data.SqlDbType.Text).Value = user[12];
                    cmd.Parameters.Add("@telefone", System.Data.SqlDbType.Text).Value = user[13];
                    cmd.Parameters.Add("@celular", System.Data.SqlDbType.Text).Value = user[14];
                    cmd.Parameters.Add("@site", System.Data.SqlDbType.Text).Value = user[15];
                    cmd.Parameters.Add("@nome_contato", System.Data.SqlDbType.Text).Value = user[16];
                    cmd.ExecuteNonQuery();
                }
                sql.Close();
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                return false;
            }


        }
        public static List<automovel> checkAutomoveis(String user)
        {
            String connectionString = Properties.Settings.Default.Fretinga;
            SqlConnection sql = new SqlConnection(connectionString);
            sql.Open();

            SqlCommand readCmd = new SqlCommand("SELECT COUNT(*) FROM automoveis WHERE [user]=@user", sql);
            readCmd.Parameters.AddWithValue("@user", user);
            SqlDataReader reader = readCmd.ExecuteReader();
            reader.Read();
            int size = reader.GetInt32(0);
            Console.WriteLine(size.ToString());
            reader.Close();
            int counter = 0;
            String[,] info = new String[2,size];
            List<automovel> lista= new List<automovel>();
            using (readCmd = new SqlCommand("SELECT * FROM automoveis WHERE [user]=@user", sql))
            {
                //readCmd.Parameters.Add("@user", System.Data.SqlDbType.VarChar).Value = user;
                readCmd.Parameters.AddWithValue("@user", user);
                reader = readCmd.ExecuteReader();
                try
                {
                    while (reader.Read())
                    {
                        automovel actual_item = new automovel();
                        actual_item.tipoV = Int32.Parse(reader["tipo"].ToString());
                        actual_item.tipoC = Int32.Parse(reader["carroceria"].ToString());
                        actual_item.carga = Int32.Parse(reader["carga"].ToString());
                        actual_item.placa = reader["placa"].ToString();
                        actual_item.nome = reader["nome"].ToString();
                        actual_item.rast = Int32.Parse(reader["rastreador"].ToString());
                        actual_item.marca = reader["marca"].ToString();
                        actual_item.modelo = reader["modelo"].ToString();
                        actual_item.ano = Int32.Parse(reader["ano"].ToString());
                        actual_item.cor = reader["cor"].ToString();
                        lista.Add(actual_item);
                        // info[0, counter] = reader["modelo"].ToString();
                        //info[1, counter] = reader["marca"].ToString();
                        //counter++;
                        //Console.WriteLine(reader["modelo"].ToString()+" - " +reader["marca"].ToString());
                        /*info[0] = reader["user"].ToString();
                        info[1] = reader["pass"].ToString();
                        info[2] = reader["email"].ToString();
                        info[3] = reader["tipo"].ToString();
                        info[4] = reader["cpf_cnpj"].ToString();
                        info[5] = reader["rg"].ToString();
                        info[6] = reader["data"].ToString();
                        info[7] = reader["nome"].ToString();
                        if (info[0].Equals(user)) userFound = true;
                        if (info[1].Equals(pass)) passFound = true;
                        Console.WriteLine(String.Format("Formato: {0}, {1}",
                        reader["user"], reader["pass"]));// etc*/
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.ToString());
                }
                //while (reader.Read())
                //{
                //Console.WriteLine("Usuário: {0}", reader.GetInt32(0).ToString());
                //}
                reader.Close();
                //userFound = true;
            }
            return lista;
        }
        public static user checkMotorista(String user, String pass)
        {
            bool userFound = false;
            bool passFound = false;
            String connectionString = Properties.Settings.Default.Fretinga;
            SqlConnection sql = new SqlConnection(connectionString);
            sql.Open();
            String[] info = new String[8];
            using (SqlCommand readCmd = new SqlCommand("SELECT * FROM table_motorista WHERE usuario = @user", sql))
            {
                //readCmd.Parameters.Add("@user", System.Data.SqlDbType.VarChar).Value = user;
                readCmd.Parameters.AddWithValue("@user", user);
                SqlDataReader reader = readCmd.ExecuteReader();
                try
                {
                    while (reader.Read())
                    {
                        info[0] = reader["nome"].ToString();
                        info[1] = reader["cpf"].ToString();
                        info[2] = reader["rg"].ToString();
                        info[3] = reader["email"].ToString();
                        info[4] = reader["celular"].ToString();
                        info[5] = reader["telefone"].ToString();
                        info[6] = reader["usuario"].ToString();
                        info[7] = reader["senha"].ToString();
                        if (info[6].Equals(user)) userFound = true;
                        if(info[7].Equals(pass))passFound = true;
                        Console.WriteLine(String.Format("Formato: {0}, {1}",
                        reader["usuario"], reader["senha"]));// etc
                    }
                }
                catch(Exception e)
                {
                    Console.WriteLine(e.ToString());
                }
                //while (reader.Read())
                //{
                    //Console.WriteLine("Usuário: {0}", reader.GetInt32(0).ToString());
                //}
                reader.Close();
                //userFound = true;
            }
            user usuario = new user();
            
            sql.Close();
            if (passFound && userFound)
            {
                Console.WriteLine("Enviando usuario");
                usuario.nome_completo = info[0];
                usuario.cpf = info[1];
                usuario.rg = info[2];
                usuario.email = info[3];
                usuario.celular = info[4];
                usuario.telefone = info[5];
                usuario.usuario = info[6];
                usuario.senha = info[7];
                return usuario;
            }
            else return null;
        }
        public static distribuidor checkDistribuidor(String email, String pass)
        {
            bool userFound = false;
            bool passFound = false;
            distribuidor dist = new distribuidor();
            String connectionString = Properties.Settings.Default.Fretinga;
            SqlConnection sql = new SqlConnection(connectionString);
            sql.Open();
            //String[] info = new String[8];
            using (SqlCommand readCmd = new SqlCommand("SELECT * FROM table_distribuidor WHERE email = @email", sql))
            {
                //readCmd.Parameters.Add("@user", System.Data.SqlDbType.VarChar).Value = user;
                readCmd.Parameters.AddWithValue("@email", email);
                SqlDataReader reader = readCmd.ExecuteReader();
                try
                {
                    while (reader.Read())
                    {
                        dist.razao_social = reader["razao_social"].ToString();
                        dist.nome_fantasia = reader["nome_fantasia"].ToString();
                        dist.cnpj = reader["cnpj"].ToString();
                        dist.inscricao_estadual = reader["inscricao_estadual"].ToString();
                        dist.pais = reader["pais"].ToString();
                        dist.estado = reader["estado"].ToString();
                        dist.cidade = reader["cidade"].ToString();
                        dist.endereco = reader["endereco"].ToString();
                        dist.bairro = reader["bairro"].ToString();
                        dist.cep = reader["cep"].ToString();
                        dist.complemento = reader["complemento"].ToString();
                        dist.email = reader["email"].ToString();
                        dist.senha = reader["senha"].ToString();
                        dist.telefone = reader["telefone"].ToString();
                        dist.celular = reader["celular"].ToString();
                        dist.site = reader["site"].ToString();
                        dist.nome_contato = reader["nome_contato"].ToString();

                        if (dist.email.Equals(email)) userFound = true;
                        if (dist.senha.Equals(pass)) passFound = true;
                        Console.WriteLine(String.Format("Formato: {0}, {1}",
                        reader["email"], reader["senha"]));// etc
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.ToString());
                }
                //while (reader.Read())
                //{
                //Console.WriteLine("Usuário: {0}", reader.GetInt32(0).ToString());
                //}
                reader.Close();
                //userFound = true;
            }
            //user usuario = new user();

            sql.Close();
            if (passFound && userFound)
            {
                Console.WriteLine("Distribuidor encontrado e autenticado !");
                return dist;
            }
            else return null;
        }

        public Program()
        {
        }

        public static void StartListening()
        {
            // Data buffer for incoming data.  
            byte[] bytes = new Byte[1024];
            // Establish the local endpoint for the socket.  
            // The DNS name of the computer  
            // running the listener is "host.contoso.com".  
            IPHostEntry ipHostInfo = Dns.Resolve(Dns.GetHostName());
            IPAddress ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 1234);

            // Create a TCP/IP socket.  
            Socket listener = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and listen for incoming connections.  
            try
            {
                listener.Bind(localEndPoint);
                listener.Listen(100);

                while (true)
                {
                    // Set the event to nonsignaled state.  
                    allDone.Reset();

                    // Start an asynchronous socket to listen for connections.  
                    Console.WriteLine("Aguardando conexão...");

                   


                    listener.BeginAccept(
                        new AsyncCallback(AcceptCallback),
                        listener);

                    // Wait until a connection is made before continuing.  
                    allDone.WaitOne();
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            Console.WriteLine("\nPress ENTER to continue...");
            Console.Read();

        }

        public static void AcceptCallback(IAsyncResult ar)
        {
            // Signal the main thread to continue.  
            allDone.Set();

            // Get the socket that handles the client request.  
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);

            // Create the state object.  
            StateObject state = new StateObject();
            state.workSocket = handler;
            handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                new AsyncCallback(ReadCallback), state);
        }

        public static void ReadCallback(IAsyncResult ar)
        {
            String content = String.Empty;

            // Retrieve the state object and the handler socket  
            // from the asynchronous state object.  
            StateObject state = (StateObject)ar.AsyncState;
            Socket handler = state.workSocket;
            // Read data from the client socket.   
            int bytesRead = handler.EndReceive(ar);

            if (bytesRead > 0)
            {
                // There  might be more data, so store the data received so far.  
                state.sb.Append(Encoding.ASCII.GetString(
                    state.buffer, 0, bytesRead));

                content = state.sb.ToString();
                
                JsonTextReader reader = new JsonTextReader(new System.IO.StringReader(content));
                int count = 0;
                reader.Read();
                reader.Read();
                if (reader.Value.ToString().Equals("updateMotoristas"))
                {
                    Console.WriteLine("Adicionando usuário..");
                    user.Clear();
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {
                                
                                user.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString() + " - " + user.Count.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    bool isUpdated = adicionarMotorista();
                    Send(handler, isUpdated.ToString());
                }
                else if (reader.Value.ToString().Equals("updateDistribuidor"))
                {
                    Console.WriteLine("Adicionando usuário..");
                    user.Clear();
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {

                                user.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString() + " - " + user.Count.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    bool isUpdated = adicionarDistribuidor();
                    Send(handler, isUpdated.ToString());
                }
                else if (reader.Value.ToString().Equals("loginInfoMotorista"))
                {
                    List < String > list = new List<String>();
                    Console.WriteLine("Checando motorista..");
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {
                                list.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString() + " - " + list.Count.ToString());

                                //user.Add(reader.Value.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    user isLogged = checkMotorista(list[0], list[1]);
                    if (isLogged != null)
                    {
                        string jsonUserData = JsonConvert.SerializeObject(isLogged);
                        Console.WriteLine(jsonUserData);
                        //if (isLogged) Console.WriteLine("Usuário encontrado !");
                        //else Console.WriteLine("Usuário não encontrado");
                        Send(handler, jsonUserData);
                    }else
                    {
                        Send(handler, "null");

                    }
                }
                else if (reader.Value.ToString().Equals("loginInfoDistribuidor"))
                {
                    List<String> list = new List<String>();
                    Console.WriteLine("Checando distribuidor..");
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {
                                list.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString() + " - " + list.Count.ToString());

                                //user.Add(reader.Value.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    distribuidor isLogged = checkDistribuidor(list[0], list[1]);
                    if (isLogged != null)
                    {
                        string jsonUserData = JsonConvert.SerializeObject(isLogged);
                        Console.WriteLine(jsonUserData);
                        //if (isLogged) Console.WriteLine("Usuário encontrado !");
                        //else Console.WriteLine("Usuário não encontrado");
                        Send(handler, jsonUserData);
                    }
                    else
                    {
                        Send(handler, "null");

                    }
                }
                else if (reader.Value.ToString().Equals("updateAutos"))
                {
                    List<String> list = new List<String>();
                    Console.WriteLine("Adicionando automovel..");
                    autoInfo.Clear();
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {
                                autoInfo.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString());
                                //user.Add(reader.Value.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    bool isUpdated = updateDBAutomoveis();
                    Send(handler, isUpdated.ToString());
                }
                else if (reader.Value.ToString().Equals("autoList"))
                {
                    List<String> list = new List<String>();
                    Console.WriteLine("Buscando lista de automoveis..");
                    autoInfo.Clear();
                    do
                    {
                        if (reader.Value != null)
                        {
                            if (count != 0 && count % 2 == 0)
                            {
                                autoInfo.Add(reader.Value.ToString());
                                Console.WriteLine(reader.Value.ToString());
                                //user.Add(reader.Value.ToString());
                            }
                            count++;
                        }
                    }
                    while (reader.Read());
                    List<automovel> automoveis = checkAutomoveis(autoInfo[0]);
                    string jsonUserData = JsonConvert.SerializeObject(automoveis);
                    Console.WriteLine(jsonUserData);
                    //if (isLogged) Console.WriteLine("Usuário encontrado !");
                    //else Console.WriteLine("Usuário não encontrado");
                    Send(handler, jsonUserData);
                }


                // }
                //else
                //{
                //  // Not all data received. Get more.  
                //    handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                //    new AsyncCallback(ReadCallback), state);
                //}
            }
        }

        private static void Send(Socket handler, String data)
        {
            // Convert the string data to byte data using ASCII encoding.  
            byte[] byteData = Encoding.ASCII.GetBytes(data);

            // Begin sending the data to the remote device.  
            handler.BeginSend(byteData, 0, byteData.Length, 0,
                new AsyncCallback(SendCallback), handler);
        }

        private static void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.  
                Socket handler = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.  
                int bytesSent = handler.EndSend(ar);
                Console.WriteLine("Sent {0} bytes to client.", bytesSent);

                handler.Shutdown(SocketShutdown.Both);
                handler.Close();

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        static void Main(string[] args)
        {
            
            StartListening();
            
        }
    }
}
